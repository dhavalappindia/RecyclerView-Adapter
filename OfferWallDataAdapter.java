package adapter;


import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ai.dailycash.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rey.material.widget.Button;

import fragment.OfferWallFragment;
import model.OfferWallDataBeen;
import utils.TimerTextView;


public class OfferWallDataAdapter extends RecyclerView.Adapter<OfferWallDataAdapter.ViewHolder> {

    Activity activity;
    OfferWallDataBeen offerWallDataBeen;
    public OnItemClick onItemClick;
    public OfferWallFragment offerWallFragment;


    public OfferWallDataAdapter(Activity activity, OfferWallFragment offerWallFragment, OnItemClick onItemClick, OfferWallDataBeen offerWallDataBeen) {
        this.onItemClick = onItemClick;
        this.offerWallDataBeen = offerWallDataBeen;
        this.activity = activity;
        this.offerWallFragment = offerWallFragment;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        viewHolder.linearLayout.setTag(i);

        viewHolder.tv_offer_completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*-----------Request Claim For Offer Wall ----------------------*/

                /*--------------Check Package is installed or not --------------------------------*/
                if (utils.Utills.isPackageExisted(activity, offerWallDataBeen.data.get(i).offer_package_name)) {

                    if (utils.Utills.isInternetAvailable(activity)) {
                        offerWallFragment.getRequestClaimOfferWallData(
                                offerWallDataBeen.data.get(i)._id,
                                true,
                                offerWallDataBeen.data.get(i).offer_name,
                                String.valueOf(offerWallDataBeen.data.get(i).offer_points));
                        Log.d("tag", "offer details package exitsted---" + offerWallDataBeen.data.get(i).offer_package_name);

                    } else {
                        Toast.makeText(activity, "No Internet Available", Toast.LENGTH_SHORT).show();

                    }



                } else {
                    if (utils.Utills.isInternetAvailable(activity)) {
                        Log.d("tag", "offer details package not exitsted---" + offerWallDataBeen.data.get(i).offer_package_name);
                        offerWallFragment.getRequestClaimOfferWallData(
                                offerWallDataBeen.data.get(i)._id,
                                false,
                                offerWallDataBeen.data.get(i).offer_name,
                                String.valueOf(offerWallDataBeen.data.get(i).offer_points));
                    }
                    else {
                        Toast.makeText(activity, "No Internet Available", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });

        Glide.with(activity).load(offerWallDataBeen.data.get(i).offer_imageurl).diskCacheStrategy(DiskCacheStrategy.ALL).into(viewHolder.categoryImage);
        viewHolder.tv_categoryName.setText(offerWallDataBeen.data.get(i).offer_name);
        viewHolder.tv_offer_points.setText(String.valueOf(offerWallDataBeen.data.get(i).offer_points ));
        viewHolder.tv_action.setText(offerWallDataBeen.data.get(i).offer_short_message);

        if (offerWallDataBeen.data.get(i).offer_status == 0) {
            viewHolder.lin_timer.setVisibility(View.GONE);
            viewHolder.tv_offer_completed.setVisibility(View.GONE);
            viewHolder.tv_action.setVisibility(View.VISIBLE);

        } else if (offerWallDataBeen.data.get(i).offer_status == 1) {
            if (!offerWallDataBeen.data.get(i).is_offer_task_completed) {

                viewHolder.lin_timer.setVisibility(View.VISIBLE);
                viewHolder.tv_offer_completed.setVisibility(View.GONE);
                viewHolder.tv_action.setVisibility(View.VISIBLE);

                /*-------------------show end time---------*/
                Log.d("tag", "offer wall adaper---offer end time---" + offerWallDataBeen.data.get(i).offer_end_time);
                long timedata = Long.parseLong(offerWallDataBeen.data.get(i).offer_end_time);
                long futureTimestamp = timedata;
                viewHolder.tv_offer_timer.setEndTime(timedata);


            } else if (offerWallDataBeen.data.get(i).is_offer_task_completed) {
                /*---------------Claim Offer---------*/
                viewHolder.lin_timer.setVisibility(View.GONE);
                viewHolder.tv_offer_completed.setVisibility(View.VISIBLE);
                viewHolder.tv_action.setVisibility(View.VISIBLE);

            }

        } else if (offerWallDataBeen.data.get(i).offer_status == 2) {

            /*-------------Offer Completed---------------*/
            viewHolder.lin_timer.setVisibility(View.GONE);
            viewHolder.tv_offer_completed.setVisibility(View.VISIBLE);
            viewHolder.tv_action.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {

        return offerWallDataBeen.data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_categoryName, tv_offer_points, tv_action;
        Button tv_offer_completed;
        TimerTextView tv_offer_timer;
        private ImageView categoryImage;
        private LinearLayout linearLayout, lin_timer;

        public ViewHolder(final View view) {
            super(view);

            tv_categoryName = view.findViewById(R.id.tv_categoryName);
            tv_offer_points = view.findViewById(R.id.tv_offer_points);
            tv_action =  view.findViewById(R.id.tv_action);
            tv_offer_timer = view.findViewById(R.id.tv_offer_timer);
            tv_offer_completed = view.findViewById(R.id.tv_offer_completed);
            lin_timer = view.findViewById(R.id.lin_timer);


            categoryImage =  view.findViewById(R.id.iv_category);

            linearLayout = view.findViewById(R.id.lin_category);
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = (int) view.getTag();

                    Log.d("tag","offer wall adapter click--");
                    onItemClick.onListClick(pos, offerWallDataBeen.data.get(pos));
                }
            });
        }
    }

    public interface OnItemClick {
        public void onListClick(int pos, OfferWallDataBeen.Data offerdata);
    }

}
