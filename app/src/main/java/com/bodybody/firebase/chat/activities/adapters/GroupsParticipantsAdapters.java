package com.bodybody.firebase.chat.activities.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bodybody.firebase.chat.activities.R;
import com.bodybody.firebase.chat.activities.managers.Screens;
import com.bodybody.firebase.chat.activities.managers.Utils;
import com.bodybody.firebase.chat.activities.models.User;
import com.bodybody.firebase.chat.activities.views.SingleClickListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
//그룹 참가자 어댑터로 유저 목록과 스크린 등을 볼 수 있습니다.
public class GroupsParticipantsAdapters extends RecyclerView.Adapter<GroupsParticipantsAdapters.ViewHolder> {

    private final Context mContext;
    private final ArrayList<User> mUsers;
    private final Screens screens;

    public GroupsParticipantsAdapters(Context mContext, ArrayList<User> usersList) {
        this.mContext = mContext;
        this.mUsers = Utils.removeDuplicates(usersList);
        this.screens = new Screens(mContext);
    }

    //뷰 홀더를 생성해 그룹 참가자 리스트 레이아웃이 보이도록 해줍니다.
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_group_participants, viewGroup, false);
        return new ViewHolder(view);
    }

    //유저의 정보 (프로필 이미지, 이미지 URL, 마지막 작성한 메세지 등)를 기본 설정해둡니다.
    // 그룹 채팅 내에 관리자를 표시해줍니다.
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final User user = mUsers.get(i);
        final String strAbout = user.getAbout();

        viewHolder.txtUsername.setText(user.getUsername());
        if (user.getUsername().equalsIgnoreCase(mContext.getString(R.string.strYou))) {
            Utils.setProfileImage(mContext, user.getMyImg(), viewHolder.imageView);
        } else {
            Utils.setProfileImage(mContext, user.getImageURL(), viewHolder.imageView);
        }

        viewHolder.txtLastMsg.setVisibility(View.VISIBLE);
        if (Utils.isEmpty(strAbout)) {
            viewHolder.txtLastMsg.setText("안녕하세요! 바디바디는 처음인데...저랑 운동 파트너 안하실래요?");
        } else {
            viewHolder.txtLastMsg.setText(strAbout);
        }

        viewHolder.txtAdmin.setVisibility(View.GONE);

        if (user.isAdmin()) {
            viewHolder.txtAdmin.setVisibility(View.VISIBLE);
        }

        viewHolder.imageView.setOnClickListener(new SingleClickListener() {
            @Override
            public void onClickView(View v) {
                screens.openViewProfileActivity(user.getId());
            }
        });

        viewHolder.itemView.setOnClickListener(new SingleClickListener() {
            @Override
            public void onClickView(View v) {
                if (!FirebaseAuth.getInstance().getCurrentUser().getUid().equalsIgnoreCase(user.getId())) {
                    screens.openUserMessageActivity(user.getId());
                }
            }
        });
    }

    //프로필 이미지, 유저 이름, 마지막 메세지, 관리자 등을 보여줍니다.
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final ImageView imageView;
        public final TextView txtUsername;
        private final TextView txtLastMsg;
        private final TextView txtAdmin;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            txtUsername = itemView.findViewById(R.id.txtUsername);
            txtLastMsg = itemView.findViewById(R.id.txtLastMsg);
            txtAdmin = itemView.findViewById(R.id.txtAdmin);
        }
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }
}
