package sinia.com.smartmart.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ToxicBakery.viewpager.transforms.ABaseTransformer;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sinia.com.smartmart.R;
import sinia.com.smartmart.activity.BuildMaterialMarketActivity;
import sinia.com.smartmart.activity.FoodShopDetailActivity;
import sinia.com.smartmart.activity.FoodStreetActivity;
import sinia.com.smartmart.activity.LoginRegisterActivity;
import sinia.com.smartmart.activity.SearchActivity;
import sinia.com.smartmart.adapter.HomeAdapter;
import sinia.com.smartmart.base.BaseFragment;
import sinia.com.smartmart.bean.UserInfo;
import sinia.com.smartmart.utils.AppInfoUtil;
import sinia.com.smartmart.utils.DialogUtils;
import sinia.com.smartmart.utils.MyApplication;
import sinia.com.smartmart.view.LocalImageHolderView;

import static sinia.com.smartmart.R.id.tv_address;

/**
 * Created by 忧郁的眼神 on 2016/9/3.
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener {

    @Bind(R.id.imgSearch)
    ImageView imgSearch;
    @Bind(R.id.listView)
    ListView listView;
    @Bind(R.id.tv_all)
    TextView tvAll;
    @Bind(R.id.img_all)
    ImageView imgAll;
    @Bind(R.id.tv_smart)
    TextView tvSmart;
    @Bind(R.id.img_smart)
    ImageView imgSmart;
    @Bind(R.id.invis)
    LinearLayout invis;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private TextView tvAddress;
    private LinearLayout ll_all, ll_smart;
    private ImageView imgFood, imgClean, imgWash, imgMore, imgJancai, imgFarm;
    private View rootView, headerView, stickyFilterView;
    private HomeAdapter adapter;
    private ArrayList<Integer> localImages = new ArrayList<Integer>();
    private ConvenientBanner convenientBanner;
    private UserInfo userBean;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_home, null);
        ButterKnife.bind(this, rootView);
        initView();
        initData();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (MyApplication.getInstance().getBoolValue("is_login")) {
            userBean = MyApplication.getInstance().getUserInfo();
            if (null != userBean) {
                tvAddress.setText(userBean.getAddress());
            }
        } else {
            tvAddress.setText("未登录");
        }
    }

    private void initData() {
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent2,
                R.color.themeColor,
                R.color.pickerview_timebtn_nor,
                R.color.triangle);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (MyApplication.getInstance().getBoolValue("is_login")) {
//                    getCartListData();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }, 4000);
                }
            }
        });
        localImages.add(AppInfoUtil.getResId("img_det", R.drawable.class));
        int h = AppInfoUtil.getScreenWidth(getActivity()) * 340 / 750;
        convenientBanner.getLayoutParams().height = h;
        String transforemerName = "StackTransformer";
        ABaseTransformer transforemer = null;
        try {
            Class cls = Class.forName("com.ToxicBakery.viewpager.transforms." + transforemerName);
            transforemer = (ABaseTransformer) cls.newInstance();
            convenientBanner.setPages(
                    new CBViewHolderCreator<LocalImageHolderView>() {
                        @Override
                        public LocalImageHolderView createHolder() {
                            return new LocalImageHolderView();
                        }
                    }, localImages).startTurning(3000).setPageIndicator(new int[]{R.drawable.carousel_point, R.drawable
                    .carousel_point_select})
                    .getViewPager().setPageTransformer(true, transforemer);
        } catch (Exception e) {
            e.printStackTrace();
        }

        adapter = new HomeAdapter(getActivity());
        listView.setAdapter(adapter);
    }

    private void initView() {
        headerView = View.inflate(getActivity(), R.layout.view_home_head, null);
        stickyFilterView = View.inflate(getActivity(), R.layout.view_home_sticky_filter, null);
        convenientBanner = (ConvenientBanner) headerView.findViewById(R.id.convenientBanner);
        tvAddress = (TextView) headerView.findViewById(tv_address);
        imgFood = (ImageView) headerView.findViewById(R.id.imgFood);
        imgFarm = (ImageView) headerView.findViewById(R.id.imgFarm);
        imgJancai = (ImageView) headerView.findViewById(R.id.imgJancai);
        imgMore = (ImageView) headerView.findViewById(R.id.imgMore);
        imgWash = (ImageView) headerView.findViewById(R.id.imgWash);
        imgClean = (ImageView) headerView.findViewById(R.id.imgClean);
        ll_smart = (LinearLayout) stickyFilterView.findViewById(R.id.ll_smart);
        ll_all = (LinearLayout) stickyFilterView.findViewById(R.id.ll_all);
        setClickListener();
        listView.addHeaderView(headerView);
        listView.addHeaderView(stickyFilterView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getActivity(), FoodShopDetailActivity.class));
            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem >= 1) {
                    invis.setVisibility(View.VISIBLE);
                } else {
                    invis.setVisibility(View.GONE);
                }

                //解决swipeRefreshLayout与listview滑动冲突
                boolean enable = false;
                if (listView != null && listView.getChildCount() > 0) {
                    // check if the first item of the list is visible
                    boolean firstItemVisible = listView.getFirstVisiblePosition() == 0;
                    // check if the top of the first item is visible
                    boolean topOfFirstItemVisible = listView.getChildAt(0).getTop() == 0;
                    // enabling or disabling the refresh layout
                    enable = firstItemVisible && topOfFirstItemVisible;
                }
                swipeRefreshLayout.setEnabled(enable);
            }
        });
    }

    private void setClickListener() {
        imgFood.setOnClickListener(this);
        imgFarm.setOnClickListener(this);
        imgJancai.setOnClickListener(this);
        imgMore.setOnClickListener(this);
        imgWash.setOnClickListener(this);
        imgClean.setOnClickListener(this);
        ll_smart.setOnClickListener(this);
        ll_all.setOnClickListener(this);
        tvAddress.setOnClickListener(this);
    }

    @OnClick({R.id.imgSearch, R.id.llAll, R.id.llSmart})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.imgSearch:
                intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.llAll:
                break;
            case R.id.llSmart:
                break;
            case R.id.ll_smart:
                break;
            case R.id.ll_all:
                break;
            case R.id.imgFood:
                intent = new Intent(getActivity(), FoodStreetActivity.class);
                startActivity(intent);
                break;
            case R.id.imgFarm:
                intent = new Intent(getActivity(), FoodStreetActivity.class);
                startActivity(intent);
                break;
            case R.id.imgJancai:
                intent = new Intent(getActivity(), BuildMaterialMarketActivity.class);
                startActivity(intent);
                break;
            case R.id.imgMore:
                listView.setSelection(1);
                break;
            case R.id.imgWash:
                DialogUtils.createFountionDevelopingTipsDialog(getActivity(), "洗衣服务正在完善中...");
                break;
            case R.id.imgClean:
                DialogUtils.createFountionDevelopingTipsDialog(getActivity(), "清洁服务正在完善中...");
                break;
            case R.id.tv_address:
                if (MyApplication.getInstance().getBoolValue("is_login")) {

                } else {
                    intent = new Intent(getActivity(), LoginRegisterActivity.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.login_open, 0);
                }
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
