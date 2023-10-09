package com.bodybody.firebase.chat.activities.adapters;

import static com.bodybody.firebase.chat.activities.constants.IConstants.TYPE_AUDIO;
import static com.bodybody.firebase.chat.activities.constants.IConstants.TYPE_CONTACT;
import static com.bodybody.firebase.chat.activities.constants.IConstants.TYPE_DOCUMENT;
import static com.bodybody.firebase.chat.activities.constants.IConstants.TYPE_IMAGE;
import static com.bodybody.firebase.chat.activities.constants.IConstants.TYPE_LOCATION;
import static com.bodybody.firebase.chat.activities.constants.IConstants.TYPE_RECORDING;
import static com.bodybody.firebase.chat.activities.constants.IConstants.TYPE_VIDEO;

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
import com.bodybody.firebase.chat.activities.models.Groups;
import com.bodybody.firebase.chat.activities.views.SingleClickListener;

import java.util.ArrayList;


//운동 파트너를 1명 이상 다수 모집할 수 있는 GroupsAdapter 입니다.
//그룹 채팅 리스트와 해당 그룹 채팅을 클릭 시 보여지는 스크린 등을 설정할 수 있습니다.

public class GroupsAdapters extends RecyclerView.Adapter<GroupsAdapters.ViewHolder> {

    private final Context mContext;
    private final ArrayList<Groups> mGroups;
    private final Screens screens;

    public GroupsAdapters(Context mContext, ArrayList<Groups> groupsList) {
        this.mContext = mContext;
        this.mGroups = groupsList;
        screens = new Screens(mContext);
    }


    //그룹 채팅 목록들을 뷰 페이지로 보여줍니다.
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_groups, viewGroup, false);
        return new ViewHolder(view);
    }

    //그룹 채팅 (모임명) 이름을 설정할 수 있고, 그에 맞는 이미지를 보여줍니다.
    //마지막으로 작성했던 날짜와 마지막으로 작성했던 메세지들은 그대로 저장해두고,
    //해당 그룹 채팅에 맞는 이미지를 변경할 수 있도록 했습니다.
    //그룹 채팅에는 오디오, 사진, 문서, 위치 등을 공유할 수 있고 메세지를 보낼 수 있습니다.
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Groups groups = mGroups.get(i);

        viewHolder.txtGroupName.setText(groups.getGroupName());

        try {
            Utils.setGroupImage(mContext, groups.getGroupImg(), viewHolder.imageView);
        } catch (Exception ignored) {
        }

        try {
            viewHolder.txtLastMsg.setVisibility(View.VISIBLE);
            viewHolder.txtLastDate.setVisibility(View.VISIBLE);
            viewHolder.imgPhoto.setVisibility(View.GONE);
        } catch (Exception e) {
            Utils.getErrors(e);
        }
        try {
            if (Utils.isEmpty(groups.getType())) {
                if (Utils.isEmpty(groups.getLastMsg())) {
                    viewHolder.txtLastMsg.setText(mContext.getString(R.string.msgTapToStartChat));
                } else {
                    viewHolder.txtLastMsg.setText(groups.getLastMsg());
                }
            } else {
                viewHolder.imgPhoto.setVisibility(View.VISIBLE);
                if (groups.getType().equalsIgnoreCase(TYPE_IMAGE)) {
                    setImageAndText(viewHolder, R.string.lblPhoto, R.drawable.ic_small_photo);
                } else if (groups.getType().equalsIgnoreCase(TYPE_RECORDING)) {
                    setImageAndText(viewHolder, R.string.lblVoiceRecording, R.drawable.ic_small_recording);
                } else if (groups.getType().equalsIgnoreCase(TYPE_AUDIO)) {
                    setImageAndText(viewHolder, R.string.lblAudio, R.drawable.ic_small_audio);
                } else if (groups.getType().equalsIgnoreCase(TYPE_VIDEO)) {
                    setImageAndText(viewHolder, R.string.lblVideo, R.drawable.ic_small_video);
                } else if (groups.getType().equalsIgnoreCase(TYPE_DOCUMENT)) {
                    setImageAndText(viewHolder, R.string.lblDocument, R.drawable.ic_small_document);
                } else if (groups.getType().equalsIgnoreCase(TYPE_CONTACT)) {
                    setImageAndText(viewHolder, R.string.lblContact, R.drawable.ic_small_contact);
                } else if (groups.getType().equalsIgnoreCase(TYPE_LOCATION)) {
                    setImageAndText(viewHolder, R.string.lblLocation, R.drawable.ic_small_location);
                } else {
                    viewHolder.imgPhoto.setVisibility(View.GONE);
                    if (Utils.isEmpty(groups.getLastMsg())) {
                        viewHolder.txtLastMsg.setText(mContext.getString(R.string.msgTapToStartChat));
                    } else {
                        viewHolder.txtLastMsg.setText(groups.getLastMsg());

                    }
                }
            }
            if (Utils.isEmpty(groups.getLastMsgTime())) {
                viewHolder.txtLastDate.setText("");
            } else {
                viewHolder.txtLastDate.setText(Utils.formatDateTime(mContext, groups.getLastMsgTime()));
            }
        } catch (Exception ignored) {

        }

        viewHolder.imageView.setOnClickListener(new SingleClickListener() {
            @Override
            public void onClickView(View v) {
                //screens.openGroupParticipantActivity(groups);
                screens.openProfilePictureActivity(groups);
            }
        });

        viewHolder.itemView.setOnClickListener(new SingleClickListener() {
            @Override
            public void onClickView(View v) {
                screens.openGroupMessageActivity(groups);
            }
        });

    }

    private void setImageAndText(ViewHolder viewHolder, int msg, int photo) {
        viewHolder.txtLastMsg.setText(mContext.getString(msg));
        viewHolder.imgPhoto.setImageResource(photo);
    }

    // 그룹 채팅 이미지 뷰에는 그룹명, 마지막 작성 날짜, 마지막 메세지, 이미지 등이 표시 됩니다.
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final ImageView imageView;
        public final TextView txtGroupName;
        private final TextView txtLastMsg;
        private final TextView txtLastDate;
        private final ImageView imgPhoto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            txtGroupName = itemView.findViewById(R.id.txtGroupName);
            txtLastMsg = itemView.findViewById(R.id.txtLastMsg);
            txtLastDate = itemView.findViewById(R.id.txtLastDate);
            imgPhoto = itemView.findViewById(R.id.imgPhoto);
        }
    }

    @Override
    public int getItemCount() {
        return mGroups.size();
    }
}
