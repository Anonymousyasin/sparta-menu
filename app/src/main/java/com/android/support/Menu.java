package com.android.support;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.view.ViewGroup;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import java.util.Arrays;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.android.support.Utils.dp;

import com.android.support.components.Colors;
import com.android.support.components.IButton;
import com.android.support.components.IButtonLink;
import com.android.support.components.ICategory;
import com.android.support.components.ICheckBox;
import com.android.support.components.ICollapse;
import com.android.support.components.IInputInt;
import com.android.support.components.IInputText;
import com.android.support.components.IRadioButton;
import com.android.support.components.ISlider;
import com.android.support.components.ISpinner;
import com.android.support.components.ISwitch;
import com.android.support.components.ITextView;
import com.android.support.entity.FeatureEntity;

import org.lsposed.lsparanoid.Obfuscate;

@Obfuscate
public class Menu extends BaseMenu {

    public static int MENU_WIDTH = 290;
    public static int POS_X = 0;
    public static int POS_Y = 100;
    public static float MENU_CORNER_RADIUS = 44f; // Sparta glass (22dp * 2 density-ish)
    boolean isExpanded = false;
    boolean overlayRequired;

    // ----------- Menu Layout ------------

    RelativeLayout __mRootContainer;
    LinearLayout __mExpanded, __featureContainer;
    LinearLayout.LayoutParams __menuExpanded;
    FrameLayout __frameLayout;
    ScrollView __scrollViewMenu;

    // ----------- Window Manager ------------
    WindowManager mWindowManager;
    WindowManager.LayoutParams vmParams;
    private WindowManager.LayoutParams canvasParams;

    // ----------- Components ------------
    DrawView drawView;
    ISwitch iSwitch;
    ISlider iSlider;
    IButton iButton;
    IInputText iInputText;
    IInputInt iInputInt;
    ICheckBox iCheckBox;
    IRadioButton iRadioButton;
    ISpinner iSpinner;
    IButtonLink iButtonLink;
    ICategory iCategory;
    ITextView iTextView;
    ICollapse iCollapse;

    //Here we write the code for our Menu
    // Reference: https://www.androidhive.info/2016/11/android-floating-widget-like-facebook-chat-head/
    public Menu(Context context) {
        super(context);
        com.android.support.components.Colors.refresh(); // apply accent preset
        drawView    = new DrawView(getContext);
        InitComponent(context);
    }

    private void InitComponent(Context context) {
        // Set Components
        iSwitch     = new ISwitch(context, typeface);
        iSlider     = new ISlider(context, typeface);
        iButton     = new IButton(context, typeface);
        iInputText  = new IInputText(context, typeface);
        iInputInt   = new IInputInt(context, typeface);
        iCheckBox   = new ICheckBox(context, typeface);
        iRadioButton = new IRadioButton(context, typeface);
        iSpinner    = new ISpinner(context, typeface);
        iButtonLink = new IButtonLink(context, typeface);
        iCategory   = new ICategory(context, typeface);
        iTextView   = new ITextView(context, typeface);
        iCollapse   = new ICollapse(context, typeface);


        __frameLayout = new FrameLayout(context); // Global markup
        __frameLayout.setOnTouchListener(onTouchListener());
        __frameLayout.setAlpha(0.4f);

        __mRootContainer = new RelativeLayout(context); // Markup on which two markups of the icon and the menu itself will be placed
        __mRootContainer.setLayoutParams(new RelativeLayout.LayoutParams(dp(context, MENU_WIDTH), WRAP_CONTENT));

        //********** The box of the mod menu **********
        __mExpanded = new LinearLayout(context); // Menu markup (when the menu is expanded)
        __mExpanded.setBackgroundColor(Colors.MENU_BG_COLOR);
        __mExpanded.setOrientation(LinearLayout.VERTICAL);
        __mExpanded.setVisibility(View.VISIBLE);
        __mExpanded.setPadding(1, 1, 1, 1); //So borders would be visible
        __mExpanded.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));

        //********** The box of the mod menu **********
        GradientDrawable gdMenuBody = new GradientDrawable();
        gdMenuBody.setCornerRadius(MENU_CORNER_RADIUS); //Set corner
        gdMenuBody.setColor(Colors.MENU_BG_COLOR); //Set background color
        gdMenuBody.setStroke(1, Colors.CARD_STROKE); // Sparta glass border
        __mExpanded.setBackground(gdMenuBody); //Apply GradientDrawable to it

        //********** Title **********
        RelativeLayout __titleLayout = new RelativeLayout(context);
        __titleLayout.setPadding(10, 5, 10, 5);
        __titleLayout.setVerticalGravity(16);
        __titleLayout.setGravity(Gravity.START);

        TextView _titleText = new TextView(context);
        _titleText.setTextColor(Colors.TEXT_COLOR);
        _titleText.setTextSize(18.0f);
        _titleText.setGravity(Gravity.START);
        _titleText.setTypeface(typeface);


        RelativeLayout.LayoutParams m_titleTextParam = new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        m_titleTextParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        _titleText.setLayoutParams(m_titleTextParam);

        //********** Bottom Text **********
        TextView _bottomText = new TextView(context);
        _bottomText.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        _bottomText.setMarqueeRepeatLimit(-1);
        _bottomText.setSingleLine(true);
        _bottomText.setSelected(true);
        _bottomText.setTextColor(Colors.TEXT_COLOR);
        _bottomText.setTypeface(typeface);
        _bottomText.setTextSize(12.0f);
        _bottomText.setGravity(Gravity.CENTER);
        _bottomText.setPadding(0, 15, 0, 15);
        _bottomText.setVisibility(View.GONE);


        //********** Minimize **********
        TextView _minimize = new TextView(context);
        _minimize.setText("▶ ");
        _minimize.setTextColor(Colors.TEXT_COLOR);
        _minimize.setTypeface(Typeface.DEFAULT_BOLD);
        _minimize.setTextSize(20.0f);
        _minimize.setPadding(10, 0, 0, 0);
        _minimize.setGravity(Gravity.CENTER_VERTICAL);
        _minimize.setOnClickListener(v -> {
            if (isExpanded) {
                isExpanded = false;
                __scrollViewMenu.setVisibility(View.GONE);
                __frameLayout.setAlpha(0.2f);

                _bottomText.setVisibility(View.GONE);
                _minimize.setText("▶ ");

            } else {
                isExpanded = true;
                __scrollViewMenu.setVisibility(View.VISIBLE);
                __frameLayout.setAlpha(1f);

                _bottomText.setVisibility(View.VISIBLE);
                _minimize.setText("▼ ");
            }
        });

        //********** Mod menu feature list **********
        __menuExpanded = new LinearLayout.LayoutParams(__mExpanded.getLayoutParams());
        __menuExpanded.weight = 1.0f;

        __scrollViewMenu = new ScrollView(context);
        __scrollViewMenu.setPadding(10, 10, 10, 10);
        __scrollViewMenu.setBackgroundColor(Colors.MENU_FEATURE_BG_COLOR);
        __scrollViewMenu.setLayoutParams(__menuExpanded);
        __scrollViewMenu.setVisibility(View.GONE);


        __featureContainer = new LinearLayout(context);
        __featureContainer.setOrientation(LinearLayout.VERTICAL);

        //********** Adding view components **********
        __mRootContainer.addView(__mExpanded);

        __titleLayout.addView(_minimize);
        __titleLayout.addView(_titleText);

        __mExpanded.addView(__titleLayout);
        __mExpanded.addView(__scrollViewMenu);
        __mExpanded.addView(_bottomText);

        __scrollViewMenu.addView(__featureContainer);

        Init(context, _titleText, _bottomText);
    }

    @Override
    public void ShowMenu() {
        __frameLayout.addView(__mRootContainer);
        __featureContainer.removeAllViews();
        featureList(GetFeatureList(), __featureContainer);
        com.android.support.components.SpartanAnim.popIn(__mRootContainer);
    }

    private native void Init(Context context, TextView title, TextView subTitle);

    @SuppressLint("WrongConstant")
    public void SetWindowManagerWindowService() {
        //Variable to check later if the phone supports Draw over other apps permission
        int mType = Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O ? 2038 : 2002;
        vmParams = new WindowManager.LayoutParams(WRAP_CONTENT,WRAP_CONTENT, mType, 8, -3);
        //params = new WindowManager.LayoutParams(WindowManager.LayoutParams.LAST_APPLICATION_WINDOW, 8, -3);
        vmParams.gravity = 51;
        vmParams.x = POS_X;
        vmParams.y = POS_Y;

        mWindowManager = (WindowManager) getContext.getSystemService(Context.WINDOW_SERVICE);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                mType,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT
        );
        canvasParams = layoutParams;
        layoutParams.gravity = Gravity.TOP | Gravity.START;
        canvasParams.x = 0;
        canvasParams.y = 100;
        if (drawView.getParent() == null) mWindowManager.addView(drawView, canvasParams);
        if (__frameLayout.getParent() == null) mWindowManager.addView(__frameLayout, vmParams);
        overlayRequired = true;
    }

    @SuppressLint("WrongConstant")
    public void SetWindowManagerActivity() {
        vmParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                POS_X,//initialX
                POS_Y,//initialy
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_OVERSCAN |
                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
                        WindowManager.LayoutParams.FLAG_SPLIT_TOUCH,
                PixelFormat.TRANSPARENT
        );
        vmParams.gravity = 51;
        vmParams.x = POS_X;
        vmParams.y = POS_Y;

        mWindowManager = ((Activity) getContext).getWindowManager();
        mWindowManager.addView(__frameLayout, vmParams);
    }

    private View.OnTouchListener onTouchListener() {
        return new View.OnTouchListener() {
            final View expandedView = __mExpanded;
            private float initialTouchX, initialTouchY;
            private int initialX, initialY;

            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = vmParams.x;
                        initialY = vmParams.y;
                        initialTouchX = motionEvent.getRawX();
                        initialTouchY = motionEvent.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        __mExpanded.setAlpha(1f);
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        __mExpanded.setAlpha(0.3f);
                        //Calculate the X and Y coordinates of the view.
                        vmParams.x = initialX + ((int) (motionEvent.getRawX() - initialTouchX));
                        vmParams.y = initialY + ((int) (motionEvent.getRawY() - initialTouchY));
                        //Update the layout with new X & Y coordinate
                        mWindowManager.updateViewLayout(__frameLayout, vmParams);
                        return true;
                    default:
                        return false;
                }
            }
        };
    }

    // ── Sparta: tab state ──
    private LinearLayout tabBarRow;
    private LinearLayout tabContentHost;
    private final java.util.LinkedHashMap<String, LinearLayout> tabPages =
            new java.util.LinkedHashMap<>();
    private int activeTabIndex = 0;
    private static final String PREF_LAST_TAB = "sparta_last_tab";

    /**
     * Tab-based layout (ImGui style): every ICategory becomes a tab;
     * its features render into that tab's own page. Uncategorized items
     * land in a "Misc" tab.
     */
    @SuppressLint("WrongConstant")
    private void featureList(String feature, LinearLayout linearLayout) {
        List<FeatureEntity> features = FeatureParser.parse(feature);

        // ── bucket features by their preceding ICategory ──
        java.util.ArrayList<String> order = new java.util.ArrayList<>();
        java.util.HashMap<String, List<FeatureEntity>> buckets = new java.util.HashMap<>();
        String current = "Misc";
        order.add(current);
        buckets.put(current, new java.util.ArrayList<>());

        for (FeatureEntity item : features) {
            if (ComponentType.valueOf(item.type) == ComponentType.ICategory) {
                current = item.name;
                if (!buckets.containsKey(current)) {
                    order.add(current);
                    buckets.put(current, new java.util.ArrayList<>());
                }
                continue; // category name becomes the tab label
            }
            buckets.get(current).add(item);
        }

        // hide tab bar if only one tab
        boolean showTabs = order.size() > 1;

        // ── build tab bar ──
        if (showTabs) {
            HorizontalScrollView tabScroll = new HorizontalScrollView(getContext);
            tabScroll.setHorizontalScrollBarEnabled(false);
            tabScroll.setBackgroundColor(Color.parseColor("#141B2E"));
            tabScroll.setLayoutParams(new LinearLayout.LayoutParams(
                    MATCH_PARENT, WRAP_CONTENT));
            tabBarRow = new LinearLayout(getContext);
            tabBarRow.setOrientation(LinearLayout.HORIZONTAL);
            tabBarRow.setPadding(12, 8, 12, 8);
            tabScroll.addView(tabBarRow);

            // insert tab bar above the scroll area in the menu body
            ViewGroup parent = (ViewGroup) linearLayout.getParent();
            int idx = parent.indexOfChild(linearLayout);
            parent.addView(tabScroll, idx);
        }

        // ── content host: one page per tab ──
        tabContentHost = new LinearLayout(getContext);
        tabContentHost.setOrientation(LinearLayout.VERTICAL);
        ViewGroup parent2 = (ViewGroup) linearLayout.getParent();
        int idx2 = parent2.indexOfChild(linearLayout);
        parent2.addView(tabContentHost, idx2 + 1);
        linearLayout.setVisibility(View.GONE); // original column unused now

        // restore last active tab
        try {
            activeTabIndex = getContext.getSharedPreferences("sparta", 0)
                    .getInt(PREF_LAST_TAB, 0);
        } catch (Exception ignored) {}

        for (int t = 0; t < order.size(); t++) {
            String cat = order.get(t);
            LinearLayout page = new LinearLayout(getContext);
            page.setOrientation(LinearLayout.VERTICAL);
            page.setVisibility(t == activeTabIndex ? View.VISIBLE : View.GONE);
            tabContentHost.addView(page);
            tabPages.put(cat, page);

            // app info goes on the first tab only
            if (t == 0) {
                iTextView.add(page, "App Package: " + getContext.getPackageName());
                iTextView.add(page, "App Name: "
                        + getContext.getApplicationInfo().loadLabel(
                                getContext.getPackageManager()));
                try {
                    iTextView.add(page, "App Version: "
                            + getContext.getPackageManager().getPackageInfo(
                                    getContext.getPackageName(), 0).versionName);
                } catch (Exception e) { e.printStackTrace(); }
            }

            for (FeatureEntity item : buckets.get(cat)) {
                if (ComponentType.valueOf(item.type) == ComponentType.ICollapse) {
                    iCollapse.add(page, item.name, item.enabled);
                    if (item.children != null && !item.children.isEmpty()) {
                        LinearLayout collapseContent = iCollapse.getCollapseContent();
                        for (FeatureEntity child : item.children) {
                            addFeatureComponent(collapseContent, child);
                        }
                    }
                } else {
                    addFeatureComponent(page, item);
                }
            }

            if (showTabs) addTabButton(cat, t);
        }

        highlightTab(activeTabIndex);
    }

    private void addTabButton(String label, final int index) {
        TextView tb = new TextView(getContext);
        tb.setText(label);
        tb.setTextSize(12);
        tb.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
        tb.setPadding(28, 14, 28, 14);
        tb.setGravity(Gravity.CENTER);
        tb.setOnClickListener(v -> {
            activeTabIndex = index;
            getContext.getSharedPreferences("sparta", 0)
                    .edit().putInt(PREF_LAST_TAB, index).apply();
            highlightTab(index);
        });
        tabBarRow.addView(tb);
    }

    private void highlightTab(int index) {
        int i = 0;
        for (String cat : tabPages.keySet()) {
            LinearLayout page = tabPages.get(cat);
            page.setVisibility(i == index ? View.VISIBLE : View.GONE);
            if (tabBarRow != null && i < tabBarRow.getChildCount()) {
                TextView tb = (TextView) tabBarRow.getChildAt(i);
                boolean on = i == index;
                tb.setTextColor(on ? Colors.accentBlend(0.5f) : Colors.TEXT_COLOR_2);
                tb.setBackground(com.android.support.components.SpartanAnim.roundBg(
                        on ? Color.parseColor("#26FFFFFF") : Color.TRANSPARENT,
                        10f, 0));
            }
            i++;
        }
        // animate the content host slightly on switch
        if (tabContentHost != null)
            com.android.support.components.SpartanAnim.bounce(tabContentHost);
    }

    private void addFeatureComponent(LinearLayout layout, FeatureEntity item) {
        ComponentType type = ComponentType.valueOf(item.type);
        switch (type) {
            case ICheckBox:
                iCheckBox.add(layout, item.id, item.name, item.description, item.enabled);
                break;
            case ISlider:
                iSlider.add(layout, item.id, item.name, item.min, item.max);
                break;
            case ISpinner:
                if (item.options != null) {
                    iSpinner.add(layout, item.id, item.name, String.join(",", item.options));
                }
                break;
            case ISwitch:
                iSwitch.add(layout, item.id, item.name, item.description, item.enabled);
                break;
            case IButton:
                iButton.add(layout, item.id, item.name);
                break;
            case IInputText:
                iInputText.add(layout, item.id, item.name);
                break;
            case IInputInt:
                iInputInt.add(layout, item.id, item.name, item.max);
                break;
            case IRadioButton:
                if (item.options != null) {
                    iRadioButton.add(layout, item.id, item.name, String.join(",", Arrays.asList(item.options)));
                }
                break;
            case IButtonLink:
                iButtonLink.add(layout, item.name, item.description);
                break;
            case ICategory:
                iCategory.add(layout, item.name);
                break;
            case ITextView:
                iTextView.add(layout, item.description);
                break;
            default:
                Log.e(TAG, "Unknown feature type: " + item.type);
                break;
        }
    }


    private boolean isViewCollapsed() {
        return !isExpanded;
    }

    public void setModMenuVisibility(boolean visible){
        if (__frameLayout != null) {
            __frameLayout.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        }
    }

    public void setEspLayoutVisibility(boolean visible){
        if (drawView != null) {
            drawView.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void KillMenu() {
        if (__frameLayout!=null) __frameLayout.removeView(__mRootContainer);
        if (__frameLayout != null && mWindowManager != null && __frameLayout.getParent()!=null) {
            mWindowManager.removeView(__frameLayout);
        }
        if (drawView != null && mWindowManager != null  && drawView.getParent()!=null) {
            mWindowManager.removeView(drawView);
        }
    }
}
