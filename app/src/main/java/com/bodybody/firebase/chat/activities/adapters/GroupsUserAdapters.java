package com.bodybody.firebase.chat.activities.adapters;

import static com.bodybody.firebase.chat.activities.constants.IConstants.ONE;
import static com.bodybody.firebase.chat.activities.constants.IConstants.STATUS_ONLINE;
import static com.bodybody.firebase.chat.activities.constants.IConstants.ZERO;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bodybody.firebase.chat.activities.R;
import com.bodybody.firebase.chat.activities.constants.IGroupListener;
import com.bodybody.firebase.chat.activities.managers.Screens;
import com.bodybody.firebase.chat.activities.managers.Utils;
import com.bodybody.firebase.chat.activities.models.Groups;
import com.bodybody.firebase.chat.activities.models.User;
import com.bodybody.firebase.chat.activities.views.SingleClickListener;
import com.bodybody.firebase.chat.activities.views.smoothcb.SmoothCheckBox;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

//그룹 유저 어댑터에는 문맥, 유저 목록, 선택된 유저 목록, 선택된 멤버 ID,
//그룹 청중,그룹 수정, 그룹, 삭제된 멤버 ID 등을 확인할 수 있습니다.

public class GroupsUserAdapters extends RecyclerView.Adapter<GroupsUserAdapters.ViewHolder>
        implements FastScrollRecyclerView.SectionedAdapter {

    private final Context mContext;
    private final ArrayList<User> mUsers;
    private final ArrayList<User> mSelectedUsers;
    private final List<String> mSelectedMembersId;
    private final IGroupListener groupListener;
    private final boolean isEditGroup;
    private final Groups groups;
    private final Set<String> mDeletedMembersId;

    public GroupsUserAdapters(Context mContext, ArrayList<User> usersList, ArrayList<User> mSelectedUsers, List<String> mSelectedMembersId, final Set<String> mDeletedMembersId, final boolean isEditGroup, final Groups groups, IGroupListener groupListener) {
        this.mContext = mContext;
        this.mUsers = Utils.removeDuplicates(usersList);
        this.mSelectedUsers = mSelectedUsers;
        this.mSelectedMembersId = mSelectedMembersId;
        this.mDeletedMembersId = mDeletedMembersId;
        this.groupListener = groupListener;

        this.isEditGroup = isEditGroup;
        this.groups = groups;

        if (isEditGroup) {
            for (int i = 0; i < mUsers.size(); i++) {
                if (groups.getMembers().contains(mUsers.get(i).getId())) {
                    this.mSelectedUsers.add(mUsers.get(i));
                    this.mSelectedMembersId.add(mUsers.get(i).getId());
                }
            }
            groupListener.setSubTitle();
        }
    }

    //그룹 유저 리스트 레이아웃과 뷰그룹을 볼 수 있는 뷰를 생성합니다.
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_users_group, viewGroup, false);
        return new ViewHolder(view);
    }

    //그룹 채팅 내의 유저와 유저 정보를 담습니다.
    //유저 이름과 프로필 이미지, 마지막 메세지 등을 설정해둡니다.
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {
        final User user = mUsers.get(position);
        final String strAbout = user.getAbout();

        viewHolder.txtUsername.setText(user.getUsername());
        Utils.setProfileImage(mContext, user.getImageURL(), viewHolder.imageView);

        viewHolder.txtLastMsg.setVisibility(View.VISIBLE);

        if (Utils.isEmpty(strAbout)) {
            viewHolder.txtLastMsg.setText("안녕하세요! 바디바디는 처음인데...저랑 운동 파트너 안하실래요?");
        } else {
            viewHolder.txtLastMsg.setText(strAbout);
        }

        //바디바디 유저가 온라인일 때는 이미지가 ON 상태로 온라인이 아닐 시에는 OFF 상태로 보여지도록 합니다.
        if (user.getIsOnline() == STATUS_ONLINE) {
            viewHolder.imgOn.setVisibility(View.VISIBLE);
            viewHolder.imgOff.setVisibility(View.GONE);
        } else {
            viewHolder.imgOn.setVisibility(View.GONE);
            viewHolder.imgOff.setVisibility(View.VISIBLE);
        }

        //체크 박스 설정에 따라 채팅 리스트도 다르게 표시되도록 합니다.
        viewHolder.cb.setOnCheckedChangeListener((checkBox, isChecked) -> user.setChecked(isChecked));

        if (isEditGroup) {
            viewHolder.cb.setChecked(groups.getMembers().contains(user.getId()));
        } else {
            viewHolder.cb.setChecked(user.isChecked());
        }

        viewHolder.imageView.setOnClickListener(new SingleClickListener() {
            @Override
            public void onClickView(View v) {
                final Screens screens = new Screens(mContext);
                screens.openViewProfileActivity(user.getId());
            }
        });

        //그룹 채팅 내에 유저 리스트에서 유저 ID를 삭제하거나 추가하도록 합니다.
        viewHolder.itemView.setOnClickListener(new SingleClickListener() {
            @Override
            public void onClickView(View v) {
                user.setChecked(!user.isChecked());
                viewHolder.cb.setChecked(user.isChecked(), true);
                if (user.isChecked()) {
                    mSelectedUsers.add(user);
                    mSelectedMembersId.add(user.getId());
                    mDeletedMembersId.remove(user.getId());
                } else {
                    mSelectedUsers.remove(user);
                    mSelectedMembersId.remove(user.getId());
                    mDeletedMembersId.add(user.getId());
                }
                groupListener.setSubTitle();
            }
        });

        //여기서 cb는 체크 박스를 의미합니다. 체크박스를 클릭해 유저 ID를 불러와 유저를 삭제하거나 추가하도록 합니다.

        viewHolder.cb.setOnClickListener(new SingleClickListener() {
            @Override
            public void onClickView(View v) {
                Utils.sout("유저를 삭제하거나 추가하고 싶다면 체크박스를 클릭하세요!");
                user.setChecked(!user.isChecked());
                viewHolder.cb.setChecked(user.isChecked(), true);
                if (user.isChecked()) {
                    mSelectedUsers.add(user);
                    mSelectedMembersId.add(user.getId());
                    mDeletedMembersId.remove(user.getId());
                } else {
                    mSelectedUsers.remove(user);
                    mSelectedMembersId.remove(user.getId());
                    mDeletedMembersId.add(user.getId());
                }
                groupListener.setSubTitle();
            }
        });

    }


    //유저가 비어있으면 유저의 현재 위치, 유저 이름 등이 보여지지 않습니다.

    @NotNull
    @Override
    public String getSectionName(int position) {
        if (!Utils.isEmpty(mUsers)) {
            return mUsers.get(position).getUsername().substring(ZERO, ONE);
        } else {
            return null;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final ImageView imageView;
        public final TextView txtUsername;
        private final ImageView imgOn;
        private final ImageView imgOff;
        private final TextView txtLastMsg;
        private final SmoothCheckBox cb;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            txtUsername = itemView.findViewById(R.id.txtUsername);
            imgOn = itemView.findViewById(R.id.imgOn);
            imgOff = itemView.findViewById(R.id.imgOff);
            txtLastMsg = itemView.findViewById(R.id.txtLastMsg);
            cb = itemView.findViewById(R.id.scb);
        }
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }
}
