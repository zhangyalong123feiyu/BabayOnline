package bibi.com.babayonlie.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import bibi.com.babayonlie.R;
import bibi.com.babayonlie.application.Constant;
import bibi.com.babayonlie.bean.EntryResultChildren;
import bibi.com.babayonlie.bean.EntryResultData;
import bibi.com.babayonlie.bean.HomeItemBean;
import bibi.com.babayonlie.bean.SchoolDetailResult;
import bibi.com.babayonlie.projecturl.projecturl;
import bibi.com.babayonlie.widget.MyCallBack;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragement_Home extends Fragment {


    private View view;
    private Button otherclass;
    private LinearLayout schoollistview;
    private List<HomeItemBean> homeItemBeen = new ArrayList<>();
    private RelativeLayout babayonline;
    private RelativeLayout everdayfood;
    private RelativeLayout wonderfutime;
    private RelativeLayout safesend;
    private RelativeLayout babayposition;
    private RelativeLayout teachplan;
    private ImageView imageright;
    private LinearLayout nologin_layout;
    private LinearLayout login_layout;

    public Fragement_Home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        initview();
        setlistioner();
        setuptrantions();
        return view;
    }

    private void setuptrantions() {
        TransitionSet transitionSet = new TransitionSet();
        transitionSet.addTransition(new ChangeBounds());
        transitionSet.addTransition(new ChangeImageTransform());
        transitionSet.setDuration(250);//设置过度时长
        getActivity().getWindow().setEnterTransition(transitionSet);//设置进入过度
        getActivity().getWindow().setExitTransition(transitionSet);
        getActivity().getWindow().setSharedElementEnterTransition(transitionSet);
        getActivity().getWindow().setSharedElementExitTransition(transitionSet);
        getActivity().getWindow().setAllowEnterTransitionOverlap(false);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Constant.logdata == null) {
            nologin_layout.setVisibility(View.VISIBLE);
            login_layout.setVisibility(View.GONE);
        } else {
            nologin_layout.setVisibility(View.GONE);
            login_layout.setVisibility(View.VISIBLE);
            initdata();
        }
    }

    private void initdata() {
        Gson gson = new Gson();
        EntryResultData entryresult = gson.fromJson(Constant.logdata, EntryResultData.class);
        List<EntryResultChildren> childrens = entryresult.getChildren();

        for (EntryResultChildren children : childrens) {
            int id = children.getSchool().getId();
            String kindergartenName = children.getSchool().getName();
            String babyName = children.getName();

            boolean flag = true;
            for (HomeItemBean item : homeItemBeen) {
                if (id == item.getId()) {
                    item.addBabyname(babyName);
                    flag = false;
                    break;
                }
            }
            if (flag) {
                // add data
                homeItemBeen.add(new HomeItemBean(id, kindergartenName, babyName));
            }
        }
        addView();
    }

    private void addView() {
        for (final HomeItemBean item : homeItemBeen) {
            View convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_homeaddschool, null);

            View view = convertView.findViewById(R.id.home_item_school);
            TextView kindergarten = (TextView) convertView.findViewById(R.id.school_textview);
            ImageView image = (ImageView) convertView.findViewById(R.id.school_imageview);
            TextView babyname = (TextView) convertView.findViewById(R.id.home_item_school_name);

            kindergarten.setText(item.getKindergarten());
            if (item.getBabyname() == null || "".equals(item.getBabyname())) {
                view.setVisibility(View.GONE);
            } else {
                babyname.setText(item.getBabyname());
            }
            lodeimage(image, item.getId());
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    ActivityOptionsCompat aop = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                            v.findViewById(R.id.school_imageview), "123");
                    Intent intent = new Intent(getActivity(), SchoolDetailActivity.class);
                    intent.putExtra("schoolId", item.getId());
                    intent.putExtra("babyNames", item.getBabyname());
                    ActivityCompat.startActivity(getActivity(), intent, aop.toBundle());
                }
            });
            schoollistview.addView(convertView);
        }
    }

    private void lodeimage(final ImageView image, int id) {
        String imei = ((TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        RequestParams requestParams = new RequestParams(projecturl.schoolurl);
        requestParams.addBodyParameter("v", "1");
        requestParams.addBodyParameter("schoolId", "" + id);
        requestParams.addBodyParameter("imei", imei);
        x.http().get(requestParams, new MyCallBack() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Gson gson = new Gson();
                SchoolDetailResult schoolinfo = gson.fromJson(s, SchoolDetailResult.class);
                String imageurl = schoolinfo.getData().getPosterUrl().getUrl();
                Log.i("imageurl", imageurl);
                x.image().bind(image, imageurl);
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                super.onError(throwable, b);
            }
        });
    }

    private void setlistioner() {
        otherclass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), OtherClassActivity.class));
            }
        });

        babayonline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), BabayOnlineActivity.class));
            }
        });
        babayposition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), BabayPostionActivity.class));
            }
        });
        safesend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SafeSendActivity.class));
            }
        });
        wonderfutime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), WonderFulTimeActivity.class));
            }
        });
        teachplan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), TeachPlanActivity.class));
            }
        });
        everdayfood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), EveryDayFoodActivity.class));
            }
        });
        imageright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), HomeInfoActivity.class));
            }
        });
    }

    private void initview() {
        nologin_layout = (LinearLayout) view.findViewById(R.id.nologin_layout);
        login_layout = (LinearLayout) view.findViewById(R.id.login_layout);

        otherclass = (Button) view.findViewById(R.id.otherclass);
        schoollistview = (LinearLayout) view.findViewById(R.id.school_listview);
        babayonline = (RelativeLayout) view.findViewById(R.id.babayonlie);
        babayposition = (RelativeLayout) view.findViewById(R.id.babaypostion);
        safesend = (RelativeLayout) view.findViewById(R.id.safesend);
        wonderfutime = (RelativeLayout) view.findViewById(R.id.wonderfultime);
        teachplan = (RelativeLayout) view.findViewById(R.id.teachplan);
        everdayfood = (RelativeLayout) view.findViewById(R.id.evereydayfood);
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText("阳光宝贝");
        ImageView imageleft = (ImageView) view.findViewById(R.id.title_imageleft);
        imageleft.setVisibility(View.GONE);
        imageright = (ImageView) view.findViewById(R.id.title_imageright);
        imageright.setVisibility(View.VISIBLE);
        imageright.setImageResource(R.drawable.parenting_inform);

    }


}
