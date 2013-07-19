package com.rong.realestateqq.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.ActionProvider;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rong.realestateqq.R;
import com.rong.realestateqq.util.DisplayUtils;

@TargetApi(14)
public class TitleBar extends RelativeLayout implements OnClickListener {
	private static final String TAG = TitleBar.class.getSimpleName();

	private View mBack;
	private TextView mTitle;
	private Fragment mFragment;
	private MenuImpl mMenuImpl;
	private Map<View, MenuItem> mMenuItemMap;
	
	public TitleBar(Context context) {
		this(context, true);
	}

	public TitleBar(Context context, boolean isCenter) {
		this(context, (Fragment) null, isCenter);
	}

	public TitleBar(Context context, Fragment fragment, boolean isCenter) {
		this(context, (AttributeSet) null, fragment, isCenter);
	}

	public TitleBar(Context context, AttributeSet attrs, boolean isCenter) {
		this(context, attrs, null, isCenter);
	}

	public TitleBar(Context context, AttributeSet attrs, Fragment fragment, boolean isCenter) {
		this(context, attrs, R.attr.titleBarStyle, fragment, isCenter);
	}

	public TitleBar(Context context, AttributeSet attrs, int defStyle,
			Fragment fragment, boolean isCenter) {
		super(context, attrs, defStyle);
		mMenuItemMap = new HashMap<View, MenuItem>();
		mFragment = fragment;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (isCenter) {
			inflater.inflate(R.layout.widget_titlebar, this);
		} else {
			inflater.inflate(R.layout.widget_titlebar_aligned, this);
		}
		initElements();
		initHomeAndBack();
		initMenuItems();
	}

	private void initElements() {
		mBack = findViewById(R.id.back);
		mTitle = (TextView) findViewById(R.id.tv_title);
		mBack.setOnClickListener(this);
//		mHome.setOnClickListener(this);
		mBack.setVisibility(View.INVISIBLE);
	}

	private void initHomeAndBack() {
		MenuItemImpl backItem = new MenuItemImpl(getContext());
		backItem.mItemId = mBack.getId();
		mMenuItemMap.put(mBack, backItem);
	}

	private void initMenuItems() {
		Context context = getContext();
		mMenuImpl = new MenuImpl(context);
		if (mFragment != null) {
			mFragment.onCreateOptionsMenu(mMenuImpl, null);
		} else if (context instanceof Activity) {
			Activity act = (Activity) context;
			act.onCreateOptionsMenu(mMenuImpl);
		}
		int lastId = 0;
		int margin = DisplayUtils.getPixel(context, 20);
		for (int i = 0; i < mMenuImpl.size(); i++) {
			MenuItem menuItem = mMenuImpl.getItem(i);
			View itemView;
			if (menuItem.getIcon() != null) {
				itemView = new ImageView(context);
				itemView.setId(menuItem.getItemId());
				((ImageView)itemView).setImageDrawable(menuItem.getIcon());
			} else {
				itemView = new Button(context);
				itemView.setId(menuItem.getItemId());
				((Button)itemView).setText(menuItem.getTitle());
				((Button)itemView).setTextSize(22);
				((Button)itemView).setTextColor(Color.WHITE);
			}
			itemView.setBackgroundResource(R.drawable.bkg_title);
			
			itemView.setOnClickListener(this);
			itemView.setPadding(margin, 0, margin, 0);
			mMenuItemMap.put(itemView, menuItem);
			addView(itemView, new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					DisplayUtils.getPixel(context, 45)));
			
			RelativeLayout.LayoutParams param = (LayoutParams) itemView
					.getLayoutParams();
			param.addRule(RelativeLayout.CENTER_VERTICAL);
			if (lastId == 0) {
				param.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			} else {
				param.addRule(RelativeLayout.LEFT_OF, lastId);
			}
			lastId = itemView.getId();
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int mode = MeasureSpec.getMode(widthMeasureSpec);
		int width = MeasureSpec.getSize(widthMeasureSpec);
		if (mode == MeasureSpec.AT_MOST) {
			setMeasuredDimension(width, getMeasuredHeight());
		}
//		int buttonWidth = Math.max(mHome.getMeasuredWidth(),
//				mBack.getMeasuredWidth());
//		int buttonCount = Math.max(1, mMenuImpl.size());
//		MarginLayoutParams params = (MarginLayoutParams) mTitle
//				.getLayoutParams();
//		// TODO
//		params.leftMargin = buttonWidth * buttonCount;
//		params.rightMargin = buttonWidth * buttonCount;
	}

	@Override
	public void onClick(View v) {
		MenuItem item = mMenuItemMap.get(v);
		if (item != null) {
			Context context = getContext();
			if (mFragment != null) {
				mFragment.onOptionsItemSelected(item);
			} else if (context instanceof Activity) {
				Activity act = (Activity) context;
				act.onOptionsItemSelected(item);
			}
		}
	}

	public void setDisplayHomeAsUpEnabled(boolean b) {
		if (b) {
			mBack.setVisibility(View.VISIBLE);
		} else {
			mBack.setVisibility(View.GONE);
		}
	}

	public TextView getTitleText() {
		return mTitle;
	}

	public void setTitle(String s) {
		mTitle.setText(s);
	}

	private class MenuImpl implements Menu {
		private Context mContext;
		private List<MenuItem> mMenuItems;

		public MenuImpl(Context context) {
			mContext = context;
			mMenuItems = new ArrayList<MenuItem>();
		}

		@Override
		public MenuItem add(CharSequence title) {
			return add(0, 0, 0, title);
		}

		@Override
		public MenuItem add(int titleRes) {
			return add(mContext.getString(titleRes));
		}

		@Override
		public MenuItem add(int groupId, int itemId, int order,
				CharSequence title) {
			MenuItemImpl item = new MenuItemImpl(mContext);
			item.mItemId = itemId;
			item.mGroupId = groupId;
			item.mTitle = title;
			mMenuItems.add(item);
			return item;
		}

		@Override
		public MenuItem add(int groupId, int itemId, int order, int titleRes) {
			return add(groupId, itemId, order, mContext.getResources()
					.getString(titleRes));
		}

		@Override
		public SubMenu addSubMenu(CharSequence title) {
			return null;
		}

		@Override
		public SubMenu addSubMenu(int titleRes) {
			return null;
		}

		@Override
		public SubMenu addSubMenu(int groupId, int itemId, int order,
				CharSequence title) {
			return null;
		}

		@Override
		public SubMenu addSubMenu(int groupId, int itemId, int order,
				int titleRes) {
			return null;
		}

		@Override
		public int addIntentOptions(int groupId, int itemId, int order,
				ComponentName caller, Intent[] specifics, Intent intent,
				int flags, MenuItem[] outSpecificItems) {
			return 0;
		}

		@Override
		public void removeItem(int id) {
		}

		@Override
		public void removeGroup(int groupId) {
		}

		@Override
		public void clear() {
		}

		@Override
		public void setGroupCheckable(int group, boolean checkable,
				boolean exclusive) {
		}

		@Override
		public void setGroupVisible(int group, boolean visible) {
		}

		@Override
		public void setGroupEnabled(int group, boolean enabled) {
		}

		@Override
		public boolean hasVisibleItems() {
			return false;
		}

		@Override
		public MenuItem findItem(int id) {
			return null;
		}

		@Override
		public int size() {
			return mMenuItems.size();
		}

		@Override
		public MenuItem getItem(int index) {
			return mMenuItems.get(index);
		}

		@Override
		public void close() {
		}

		@Override
		public boolean performShortcut(int keyCode, KeyEvent event, int flags) {
			return false;
		}

		@Override
		public boolean isShortcutKey(int keyCode, KeyEvent event) {
			return false;
		}

		@Override
		public boolean performIdentifierAction(int id, int flags) {
			return false;
		}

		@Override
		public void setQwertyMode(boolean isQwerty) {
		}
	}

	private class MenuItemImpl implements MenuItem {
		private int mItemId;
		private int mGroupId;
		private CharSequence mTitle;
		private Drawable mIcon;
		private Context mContext;

		public MenuItemImpl(Context context) {
			mContext = context;
		}

		@Override
		public int getItemId() {
			return mItemId;
		}

		@Override
		public int getGroupId() {
			return mGroupId;
		}

		@Override
		public int getOrder() {
			return 0;
		}

		@Override
		public MenuItem setTitle(CharSequence title) {
			mTitle = title;
			return this;
		}

		@Override
		public MenuItem setTitle(int title) {
			return setTitle(mContext.getString(title));
		}

		@Override
		public CharSequence getTitle() {
			return mTitle;
		}

		@Override
		public MenuItem setTitleCondensed(CharSequence title) {
			return null;
		}

		@Override
		public CharSequence getTitleCondensed() {
			return null;
		}

		@Override
		public MenuItem setIcon(Drawable icon) {
			mIcon = icon;
			return this;
		}

		@Override
		public MenuItem setIcon(int iconRes) {
			return setIcon(mContext.getResources().getDrawable(iconRes));
		}

		@Override
		public Drawable getIcon() {
			return mIcon;
		}

		@Override
		public MenuItem setIntent(Intent intent) {
			return null;
		}

		@Override
		public Intent getIntent() {
			return null;
		}

		@Override
		public MenuItem setShortcut(char numericChar, char alphaChar) {
			return null;
		}

		@Override
		public MenuItem setNumericShortcut(char numericChar) {
			return null;
		}

		@Override
		public char getNumericShortcut() {
			return 0;
		}

		@Override
		public MenuItem setAlphabeticShortcut(char alphaChar) {
			return null;
		}

		@Override
		public char getAlphabeticShortcut() {
			return 0;
		}

		@Override
		public MenuItem setCheckable(boolean checkable) {
			return null;
		}

		@Override
		public boolean isCheckable() {
			return false;
		}

		@Override
		public MenuItem setChecked(boolean checked) {
			return null;
		}

		@Override
		public boolean isChecked() {
			return false;
		}

		@Override
		public MenuItem setVisible(boolean visible) {
			return null;
		}

		@Override
		public boolean isVisible() {
			return false;
		}

		@Override
		public MenuItem setEnabled(boolean enabled) {
			return null;
		}

		@Override
		public boolean isEnabled() {
			return false;
		}

		@Override
		public boolean hasSubMenu() {
			return false;
		}

		@Override
		public SubMenu getSubMenu() {
			return null;
		}

		@Override
		public MenuItem setOnMenuItemClickListener(
				OnMenuItemClickListener menuItemClickListener) {
			return null;
		}

		@Override
		public ContextMenuInfo getMenuInfo() {
			return null;
		}

		@Override
		public void setShowAsAction(int actionEnum) {
		}

		@Override
		public MenuItem setShowAsActionFlags(int actionEnum) {
			return null;
		}

		@Override
		public MenuItem setActionView(View view) {
			return null;
		}

		@Override
		public MenuItem setActionView(int resId) {
			return null;
		}

		@Override
		public View getActionView() {
			return null;
		}

		@Override
		public MenuItem setActionProvider(ActionProvider actionProvider) {
			return null;
		}

		@Override
		public ActionProvider getActionProvider() {
			return null;
		}

		@Override
		public boolean expandActionView() {
			return false;
		}

		@Override
		public boolean collapseActionView() {
			return false;
		}

		@Override
		public boolean isActionViewExpanded() {
			return false;
		}

		@Override
		public MenuItem setOnActionExpandListener(
				OnActionExpandListener listener) {
			return null;
		}
	}
}
