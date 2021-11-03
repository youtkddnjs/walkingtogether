package com.swsoft.walkingtogether;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class roomlist_apater extends RecyclerView.Adapter {

    Context context;
    ArrayList<roomlist_item> items;

    public roomlist_apater(Context context, ArrayList<roomlist_item> items) {
        this.context = context;
        this.items = items;
    }

    //화면에 보여질 뷰1개를 만들어야 될대 자동으로 호출되는 메소드
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
                        //만들어질 모양의 레이아웃 , 만들어질 뷰가 붙을 부모 뷰 , 지금 당장 붙일거냐?)
        View itemView = inflater.inflate(R.layout.roomlist_item, parent, false); //참조 변수 타입 View 인 이유는 어떤 뷰만들어도 참조가능
        VH holder = new VH(itemView);
        return holder;
    }

    //위에서 만든 아이템 뷰(여기말하는 뷰는 카드뷰)안에 있는 자식 뷰들(버튼들)에 값(글씨)들을 연결해야 할때 자동으로 실행되는 메소드
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VH vh = (VH)holder;
       roomlist_item item = items.get(position); //대입할 데이터 요소객체(Item) 얻어오기

       vh.title_view.setText(item.title);
       vh.time_view.setText(item.time);
       vh.goal_view.setText(item.goal);

    }

    //Adapter가 만들 뷰갯수 리턴 (리사이클러가 돌아갈 횟수)
    @Override
    public int getItemCount() {
        return items.size();
    }

    class VH extends RecyclerView.ViewHolder{
        Button title_view;
        Button time_view;
        Button goal_view;

        public VH(@NonNull View itemView) {
            super(itemView);
            title_view = itemView.findViewById(R.id.roomtitle);
            time_view = itemView.findViewById(R.id.roomtime);
            goal_view = itemView.findViewById(R.id.roomgoal);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //1. 아답터안에서 클릭된 항목뷰의 위치
                    int position = getLayoutPosition();
                    //2. 그리고 위치에 대한 요소 값
                    roomlist_item item = items.get(position);

                    Toast.makeText(context, "item", Toast.LENGTH_SHORT).show();
//                    roomlist_item item = items.get(getLayoutPosition()); //3. 1,2 한줄로 표시

//                    Intent intent = new Intent(context, roominfo.class);
//                    //intent로 전달
//                    intent.putExtra("title",item.title);
//                    intent.putExtra("time",item.time);
//                    intent.putExtra("goal",item.goal);
//
//                    context.startActivity(intent);


                }
            });// itemView.setOnClickListener

            title_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "item", Toast.LENGTH_SHORT).show();
                    roomlist_item item = items.get(getLayoutPosition()); //3. 1,2 한줄로 표시
                    Intent intent = new Intent(context, roominfo.class);
                    //intent로 전달
                    intent.putExtra("title",item.title);
                    intent.putExtra("time",item.time);
                    intent.putExtra("goal",item.goal);

                    context.startActivity(intent);
                }
            });




        }//생성자

    }//VH


}
