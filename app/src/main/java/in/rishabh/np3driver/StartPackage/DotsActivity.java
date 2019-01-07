package in.rishabh.np3driver.StartPackage;

/**
 * Created by Rishabh Nayak on 01-11-2018.
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.matthewtamlin.android_utilities_library.helpers.BitmapEfficiencyHelper;
import com.matthewtamlin.android_utilities_library.helpers.ScreenSizeHelper;
import com.matthewtamlin.sliding_intro_screen_library.background.BackgroundManager;
import com.matthewtamlin.sliding_intro_screen_library.background.ColorBlender;
import com.matthewtamlin.sliding_intro_screen_library.buttons.IntroButton;
import com.matthewtamlin.sliding_intro_screen_library.core.IntroActivity;
import com.matthewtamlin.sliding_intro_screen_library.indicators.SelectionIndicator;
import com.matthewtamlin.sliding_intro_screen_library.pages.ParallaxPage;
import com.matthewtamlin.sliding_intro_screen_library.transformers.MultiViewParallaxTransformer;

import java.util.ArrayList;
import java.util.Collection;

import in.rishabh.np3driver.R;
import in.rishabh.np3driver.RegistrationPackage.LoginActivity;


public class DotsActivity extends IntroActivity {

    private static final int[] BACKGROUND_COLORS = {0xffFFCC33, 0xffFFCC33,0xffFFCC33};

    public static final String DISPLAY_ONCE_PREFS = "display_only_once_spfile";

    public static final String DISPLAY_ONCE_KEY = "display_only_once_spkey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.NoActionBar); // Looks good when the status bar is hidden
        super.onCreate(savedInstanceState);

        // Skip to the next Activity if the user has previously completed the introduction
        if (introductionCompletedPreviously()) {
            final Intent nextActivity = new Intent(this, LoginActivity.class);
            startActivity(nextActivity);
        }

        hideStatusBar();
        configureTransformer();
        configureBackground();

    }

    @Override
    protected Collection<Fragment> generatePages(Bundle savedInstanceState) {
        // This variable holds the pages while they are being created

        final ArrayList<Fragment> pages = new ArrayList<>();

        // Get the screen dimensions so that Bitmaps can be loaded efficiently
        final int screenWidth = ScreenSizeHelper.getScreenWidthPx(this);
        final int screenHeight = ScreenSizeHelper.getScreenHeightPx(this);

        // Load the Bitmap resources into memory
        final Bitmap frontDots = BitmapEfficiencyHelper.decodeResource(this, R.drawable.s3,
                screenWidth, screenHeight);
        final Bitmap backDots1 = BitmapEfficiencyHelper.decodeResource(this, R.drawable.s4,
                screenWidth, screenHeight);
        final Bitmap backDots2 = BitmapEfficiencyHelper.decodeResource(this, R.drawable.s5,
                screenWidth, screenHeight);
//        final Bitmap backDots2 = BitmapEfficiencyHelper.decodeResource(this, R.drawable.image,
//                screenWidth, screenHeight);
//        final Bitmap backDots3 = BitmapEfficiencyHelper.decodeResource(this, R.drawable.image,
//                screenWidth, screenHeight);

        // Create as many pages as there are background colors

            final ParallaxPage newPage = ParallaxPage.newInstance();
            newPage.setFrontImage(frontDots);
            newPage.setBackImage(frontDots);
            pages.add(newPage);

            final ParallaxPage newPage1 = ParallaxPage.newInstance();
            newPage1.setFrontImage(backDots1);
            newPage1.setBackImage(backDots1);
            pages.add(newPage1);

        final ParallaxPage newPage2 = ParallaxPage.newInstance();
        newPage2.setFrontImage(backDots2);
        newPage2.setBackImage(backDots2);
        pages.add(newPage2);


        return pages;
    }

    public void test(){

    }

    @Override
    protected IntroButton.Behaviour generateFinalButtonBehaviour() {
		/* The pending changes to the shared preferences editor will be applied when the
		 * introduction is successfully completed. By setting a flag in the pending edits and
		 * checking the status of the flag when the activity starts, the introduction screen can
		 * be skipped if it has previously been completed.
		 */
        final SharedPreferences sp = getSharedPreferences(DISPLAY_ONCE_PREFS, MODE_PRIVATE);
        final SharedPreferences.Editor pendingEdits = sp.edit().putBoolean(DISPLAY_ONCE_KEY, true);

        // Define the next activity intent and create the Behaviour to use for the final button
        final Intent nextActivity = new Intent(this, LoginActivity.class);
        return new IntroButton.ProgressToNextActivity(nextActivity, pendingEdits);
    }

    @Override
    public SelectionIndicator getProgressIndicator() {
        return super.getProgressIndicator();
    }

    private boolean introductionCompletedPreviously() {
        final SharedPreferences sp = getSharedPreferences(DISPLAY_ONCE_PREFS, MODE_PRIVATE);
        return sp.getBoolean(DISPLAY_ONCE_KEY, false);
    }

    private void configureTransformer() {
        final MultiViewParallaxTransformer transformer = new MultiViewParallaxTransformer();
        transformer.withParallaxView(R.id.page_fragment_imageHolderFront, 1.2f);
        setPageTransformer(false, transformer);
    }

    private void configureBackground() {
        final BackgroundManager backgroundManager = new ColorBlender(BACKGROUND_COLORS);
        setBackgroundManager(backgroundManager);
    }
}