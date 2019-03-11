package in.rishabh.np3driver;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import in.rishabh.np3driver.Pojo.RidesPojo;


public class RideListAdapter  extends RecyclerView.Adapter<RideListAdapter.FinalorderViewHolder>{

    private Context context;
    List<RidesPojo> data;
    public RideListAdapter(Context context, List<RidesPojo> data){
        this.context=context;
        this.data=data;
    }

    @Override
    public FinalorderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.finalorder_list_layout,parent,false);
        return new FinalorderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FinalorderViewHolder holder, int position) {
        RidesPojo passengerList= data.get(position);
        holder.rideid.setText("Ride ID : CG/R/2019/"+passengerList.getRideid());
        holder.amount.setText("Amount : "+passengerList.getAmount());
        holder.status.setText("Status : "+passengerList.getStatus());
        holder.distance.setText("Distance : "+passengerList.getKm());
        if (passengerList.getRideid()==null){
            holder.cardView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        try{return data.size();}
        catch (Exception e){

        }
        return 0;
    }

    public class FinalorderViewHolder extends RecyclerView.ViewHolder {

        TextView rideid,amount,distance,status;
        CardView cardView;
        public FinalorderViewHolder(View itemView) {
            super(itemView);
            rideid=itemView.findViewById(R.id.rideid);
            amount=itemView.findViewById(R.id.amount);
            distance=itemView.findViewById(R.id.distance);
            status=itemView.findViewById(R.id.status);
            cardView=itemView.findViewById(R.id.card);
        }
    }
}