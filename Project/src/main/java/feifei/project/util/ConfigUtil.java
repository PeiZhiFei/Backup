package feifei.project.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.Set;

public class ConfigUtil
{
    private static String path = "sp_file";

    public static boolean write (Context context, String key, String value)
    {
        SharedPreferences share = context.getSharedPreferences (path,
                Context.MODE_PRIVATE);
        Editor editor = share.edit ();
        return editor.putString (key, value).commit ();
    }

    public static boolean write (Context context, String key, boolean value)
    {
        SharedPreferences share = context.getSharedPreferences (path,
                Context.MODE_PRIVATE);
        Editor editor = share.edit ();
        return editor.putBoolean (key, value).commit ();
    }

    public static boolean write (Context context, String key, int value)
    {
        SharedPreferences share = context.getSharedPreferences (path,
                Context.MODE_PRIVATE);
        Editor editor = share.edit ();
        return editor.putInt (key, value).commit ();
    }

    public static boolean write (Context context, String key, long value)
    {
        SharedPreferences share = context.getSharedPreferences (path,
                Context.MODE_PRIVATE);
        Editor editor = share.edit ();
        return editor.putLong (key, value).commit ();
    }

    public static boolean write (Context context, String key, float value)
    {
        SharedPreferences share = context.getSharedPreferences (path,
                Context.MODE_PRIVATE);
        Editor editor = share.edit ();
        return editor.putFloat (key, value).commit ();
    }

    public static boolean write (Context context, String key, Set<String> set)
    {
        SharedPreferences share = context.getSharedPreferences (path,
                Context.MODE_PRIVATE);
        Editor editor = share.edit ();
        return editor.putStringSet (key, set).commit ();
    }

    public static void apply (Context context, String key, String value)
    {
        SharedPreferences share = context.getSharedPreferences (path,
                Context.MODE_PRIVATE);
        Editor editor = share.edit ();
        editor.putString (key, value).apply ();
    }

    public static void apply (Context context, String key, boolean value)
    {
        SharedPreferences share = context.getSharedPreferences (path,
                Context.MODE_PRIVATE);
        Editor editor = share.edit ();
        editor.putBoolean (key, value).apply ();
    }

    public static void apply (Context context, String key, int value)
    {
        SharedPreferences share = context.getSharedPreferences (path,
                Context.MODE_PRIVATE);
        Editor editor = share.edit ();
        editor.putInt (key, value).apply ();
    }

    public static void apply (Context context, String key, long value)
    {
        SharedPreferences share = context.getSharedPreferences (path,
                Context.MODE_PRIVATE);
        Editor editor = share.edit ();
        editor.putLong (key, value).apply ();
    }

    public static void apply (Context context, String key, float value)
    {
        SharedPreferences share = context.getSharedPreferences (path,
                Context.MODE_PRIVATE);
        Editor editor = share.edit ();
        editor.putFloat (key, value).apply ();
    }

    public static void apply (Context context, String key, Set<String> set)
    {
        SharedPreferences share = context.getSharedPreferences (path,
                Context.MODE_PRIVATE);
        Editor editor = share.edit ();
        editor.putStringSet (key, set).apply ();
    }

    public static int readInt (Context context, String key, int defaultValue)
    {
        SharedPreferences share = context.getSharedPreferences (path,
                Context.MODE_PRIVATE);
        return share.getInt (key, defaultValue);
    }

    public static String readString (Context context, String key,
                                     String defaultValue)
    {
        SharedPreferences share = context.getSharedPreferences (path,
                Context.MODE_PRIVATE);
        return share.getString (key, defaultValue);
    }

    public static boolean readBoolean (Context context, String key,
                                       boolean defaultValue)
    {
        SharedPreferences share = context.getSharedPreferences (path,
                Context.MODE_PRIVATE);
        return share.getBoolean (key, defaultValue);
    }

    public static float readFloat (Context context, String key,
                                   float defaultValue)
    {
        SharedPreferences share = context.getSharedPreferences (path,
                Context.MODE_PRIVATE);
        return share.getFloat (key, defaultValue);
    }

    public static long readLong (Context context, String key, long defaultValue)
    {
        SharedPreferences share = context.getSharedPreferences (path,
                Context.MODE_PRIVATE);
        return share.getLong (key, defaultValue);
    }

    public static Set<String> readSet (Context context, String key,
                                       Set<String> defaultValue)
    {
        SharedPreferences share = context.getSharedPreferences (path,
                Context.MODE_PRIVATE);
        return share.getStringSet (key, defaultValue);
    }

}
