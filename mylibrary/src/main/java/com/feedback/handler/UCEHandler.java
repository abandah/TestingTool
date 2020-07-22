package com.feedback.handler;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Deque;
import java.util.List;
import java.util.Locale;

public final class UCEHandler {
    public static final String eXTRA_ErrorMessage = "ErrorMessage";
    public static final String eXTRA_StackTrace = "StackTrace";
    static final String eXTRA_STACK_TRACE = "eXTRA_STACK_TRACE";
    static final String eXTRA_Error_Page = "Error_Page";
    static final String eXTRA_ACTIVITY_LOG = "eXTRA_ACTIVITY_LOG";
    private final static String TAG = "UCEHandler";
    private static final String DEFAULT_HANDLER_PACKAGE_NAME = "com.android.internal.os";
    private static final String DEFAULT_ANDROID_PACKAGE_NAME = getDEFAULT_ANDROID_PACKAGE_NAME();
    private static final int MAX_STACK_TRACE_SIZE = 131071; //128 KB - 1
    private static final int MAX_ACTIVITIES_IN_LOG = 50;
    private static final String SHARED_PREFERENCES_FILE = "uceh_preferences";
    private static final String SHARED_PREFERENCES_FIELD_TIMESTAMP = "last_crash_timestamp";
    private static final Deque<String> activityLog = new ArrayDeque<>(MAX_ACTIVITIES_IN_LOG);
    static String commaSeparatedEmailAddresses;
    private static String uceHandlerPackageName = "";
    public static String  Link = "";
    private static String preflix = "\n\tat ";
    @SuppressLint("StaticFieldLeak")
    private static Application application;
    private static boolean isInBackground = true;
    private static boolean isBackgroundMode;
    private static boolean isUCEHEnabled;
    private static boolean isTrackActivitiesEnabled;
    private static WeakReference<Activity> lastActivityCreated = new WeakReference<>(null);


    UCEHandler(Builder builder) {
        isUCEHEnabled = builder.isUCEHEnabled;
        isTrackActivitiesEnabled = builder.isTrackActivitiesEnabled;
        isBackgroundMode = builder.isBackgroundModeEnabled;
        commaSeparatedEmailAddresses = builder.commaSeparatedEmailAddresses;
        uceHandlerPackageName = getApplicationName(builder.context);
        this.Link = builder.Link;
        setUCEHandler(builder.context);
    }

    private static void setUCEHandler(final Context context) {
        try {
            if (context != null) {
                final Thread.UncaughtExceptionHandler oldHandler = Thread.getDefaultUncaughtExceptionHandler();
                if (oldHandler != null && oldHandler.getClass().getName().startsWith(uceHandlerPackageName)) {
                } else {
                    if (oldHandler != null && !oldHandler.getClass().getName().startsWith(DEFAULT_HANDLER_PACKAGE_NAME)) {
                    }
                    application = (Application) context.getApplicationContext();
                    //Setup UCE Handler.
                    Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                        @Override
                        public void uncaughtException(Thread thread, final Throwable throwable) {
                            if (isUCEHEnabled) {
                                if (hasCrashedInTheLastSeconds(application)) {
                                    if (oldHandler != null) {
                                        oldHandler.uncaughtException(thread, throwable);
                                        return;
                                    }
                                } else {
                                    setLastCrashTimestamp(application, new Date().getTime());
                                    if (!isInBackground || isBackgroundMode) {
                                        final Intent intent = new Intent(application, UCEDefaultActivity.class);
                                        StringWriter sw = new StringWriter();
                                        PrintWriter pw = new PrintWriter(sw);
                                        throwable.printStackTrace(pw);
                                        String stackTraceString = sw.toString();
                                        String MainstackTraceString = stackTraceString;
                                        List<String> strings2 = Arrays.asList(stackTraceString.split(preflix));
                                        ArrayList<String> strings = new ArrayList<>();
                                        ArrayList<String> error = new ArrayList<>();
                                        // ArrayList<String> strings = new ArrayList<>();
                                        for (int i = 0; i < strings2.size(); i++) {
                                            if (!strings2.get(i).matches(DEFAULT_ANDROID_PACKAGE_NAME)) {
                                                strings.add(strings2.get(i));
                                            }
                                            if (strings2.get(i).startsWith(uceHandlerPackageName)) {
                                                error.add(strings2.get(i));
                                            }

                                        }
                                        stackTraceString = TextUtils.join(preflix, strings);
                                        if (stackTraceString.length() > MAX_STACK_TRACE_SIZE) {
                                            String disclaimer = " [stack trace too large]";
                                            stackTraceString = stackTraceString.substring(0, MAX_STACK_TRACE_SIZE - disclaimer.length()) + disclaimer;
                                        }
                                        intent.putExtra(eXTRA_STACK_TRACE, MainstackTraceString);
                                        if (isTrackActivitiesEnabled) {
                                            StringBuilder activityLogStringBuilder = new StringBuilder();
                                            while (!activityLog.isEmpty()) {
                                                activityLogStringBuilder.append(activityLog.poll());
                                            }
                                            intent.putExtra(eXTRA_ACTIVITY_LOG, activityLogStringBuilder.toString());
                                        }


                                        intent.putExtra(eXTRA_Error_Page, error.get(0));//;getErrorpage(error));
                                        intent.putExtra(eXTRA_ErrorMessage, strings2.get(0));
                                        intent.putExtra(eXTRA_StackTrace, MainstackTraceString);

                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        application.startActivity(intent);
                                    } else {
                                        if (oldHandler != null) {
                                            oldHandler.uncaughtException(thread, throwable);
                                            return;
                                        }
                                        //If it is null (should not be), we let it continue and kill the process or it will be stuck
                                    }
                                }
                                final Activity lastActivity = lastActivityCreated.get();
                                if (lastActivity != null) {
                                    lastActivity.finish();
                                    lastActivityCreated.clear();
                                }
                                killCurrentProcess();
                            } else if (oldHandler != null) {
                                //Pass control to old uncaught exception handler
                                oldHandler.uncaughtException(thread, throwable);
                            }
                        }
                    });
                    application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
                        final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                        int currentlyStartedActivities = 0;

                        @Override
                        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                            if (activity.getClass() != UCEDefaultActivity.class) {
                                lastActivityCreated = new WeakReference<>(activity);
                            }
                            if (isTrackActivitiesEnabled) {
                                activityLog.add(dateFormat.format(new Date()) + ": " + activity.getClass().getSimpleName() + " created\n");
                            }
                        }

                        @Override
                        public void onActivityStarted(Activity activity) {
                            currentlyStartedActivities++;
                            isInBackground = (currentlyStartedActivities == 0);
                        }

                        @Override
                        public void onActivityResumed(Activity activity) {
                            if (isTrackActivitiesEnabled) {
                                activityLog.add(dateFormat.format(new Date()) + ": " + activity.getClass().getSimpleName() + " resumed\n");
                            }
                        }

                        @Override
                        public void onActivityPaused(Activity activity) {
                            if (isTrackActivitiesEnabled) {
                                activityLog.add(dateFormat.format(new Date()) + ": " + activity.getClass().getSimpleName() + " paused\n");
                            }
                        }

                        @Override
                        public void onActivityStopped(Activity activity) {
                            currentlyStartedActivities--;
                            isInBackground = (currentlyStartedActivities == 0);
                        }

                        @Override
                        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                        }

                        @Override
                        public void onActivityDestroyed(Activity activity) {
                            if (isTrackActivitiesEnabled) {
                                activityLog.add(dateFormat.format(new Date()) + ": " + activity.getClass().getSimpleName() + " destroyed\n");
                            }
                        }
                    });
                }
            } else {
            }
        } catch (Throwable throwable) {
        }
    }

    private static String getErrorpage(ArrayList<String> throwable) {

        String s= TextUtils.join("|",throwable);
        return s;
    }

    /**
     * INTERNAL method that tells if the app has crashed in the last seconds.
     * This is used to avoid restart loops.
     *
     * @return true if the app has crashed in the last seconds, false otherwise.
     */
    private static boolean hasCrashedInTheLastSeconds(Context context) {
        long lastTimestamp = getLastCrashTimestamp(context);
        long currentTimestamp = new Date().getTime();
        return (lastTimestamp <= currentTimestamp && currentTimestamp - lastTimestamp < 3000);
    }

    @SuppressLint("ApplySharedPref")
    private static void setLastCrashTimestamp(Context context, long timestamp) {
        context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE).edit().putLong(SHARED_PREFERENCES_FIELD_TIMESTAMP, timestamp).commit();
    }

    private static void killCurrentProcess() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
    }

    private static long getLastCrashTimestamp(Context context) {
        return context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE).getLong(SHARED_PREFERENCES_FIELD_TIMESTAMP, -1);
    }

    static void closeApplication(Activity activity) {
        activity.finish();
        killCurrentProcess();
    }

    static void RestartApplication(Activity activity) {
        Intent launchIntent = activity.getPackageManager().getLaunchIntentForPackage(uceHandlerPackageName);
        activity.startActivity( launchIntent );
        activity.finish();
        killCurrentProcess();
    }

    private static String getDEFAULT_ANDROID_PACKAGE_NAME() {
        ArrayList<String> strings = new ArrayList<String>();
        strings.add("com.android.");
        strings.add("android.");
        strings.add("java.");


        String s = TextUtils.join("|", strings);
        s = "(" + s + ").*";
        return s;

    }

    public String getApplicationName(Context context) {
        return context.getApplicationContext().getPackageName();
    }

    public static class Builder {
        private Context context;
        private boolean isUCEHEnabled = true;
        private String commaSeparatedEmailAddresses;
        private String Link;
        private boolean isTrackActivitiesEnabled = false;
        private boolean isBackgroundModeEnabled = true;
        public Builder(Context context) {
            this.context = context;
        }

        public Builder setUCEHEnabled(boolean isUCEHEnabled) {
            this.isUCEHEnabled = isUCEHEnabled;
            return this;
        }

        public Builder setTrackActivitiesEnabled(boolean isTrackActivitiesEnabled) {
            this.isTrackActivitiesEnabled = isTrackActivitiesEnabled;
            return this;
        }

        public Builder setBackgroundModeEnabled(boolean isBackgroundModeEnabled) {
            this.isBackgroundModeEnabled = isBackgroundModeEnabled;
            return this;
        }

        public Builder addCommaSeparatedEmailAddresses(String commaSeparatedEmailAddresses) {
            this.commaSeparatedEmailAddresses = (commaSeparatedEmailAddresses != null) ? commaSeparatedEmailAddresses : "";
            return this;
        }

        public UCEHandler build() {
            return new UCEHandler(this);
        }

        public Builder setLink(String link) {
            this.Link=link;
            return this;
        }
    }
}
