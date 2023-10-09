package com.bodybody.firebase.chat.activities.adapters;

import static com.bodybody.firebase.chat.activities.constants.IConstants.BROADCAST_DOWNLOAD_EVENT;
import static com.bodybody.firebase.chat.activities.constants.IConstants.COMPLETED;
import static com.bodybody.firebase.chat.activities.constants.IConstants.DOWNLOAD_DATA;
import static com.bodybody.firebase.chat.activities.constants.IConstants.SLASH;
import static com.bodybody.firebase.chat.activities.constants.IConstants.TYPE_AUDIO;
import static com.bodybody.firebase.chat.activities.constants.IConstants.TYPE_CONTACT;
import static com.bodybody.firebase.chat.activities.constants.IConstants.TYPE_DOCUMENT;
import static com.bodybody.firebase.chat.activities.constants.IConstants.TYPE_IMAGE;
import static com.bodybody.firebase.chat.activities.constants.IConstants.TYPE_LOCATION;
import static com.bodybody.firebase.chat.activities.constants.IConstants.TYPE_RECORDING;
import static com.bodybody.firebase.chat.activities.constants.IConstants.TYPE_TEXT;
import static com.bodybody.firebase.chat.activities.constants.IConstants.TYPE_VIDEO;
import static com.bodybody.firebase.chat.activities.constants.IConstants.ZERO;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bodybody.firebase.chat.activities.R;
import com.bodybody.firebase.chat.activities.managers.Screens;
import com.bodybody.firebase.chat.activities.managers.Utils;
import com.bodybody.firebase.chat.activities.models.Chat;
import com.bodybody.firebase.chat.activities.models.DownloadFileEvent;
import com.bodybody.firebase.chat.activities.models.LocationAddress;
import com.bodybody.firebase.chat.activities.models.User;
import com.bodybody.firebase.chat.activities.views.SingleClickListener;
import com.bodybody.firebase.chat.activities.views.audiowave.AudioPlayerView;
import com.bodybody.firebase.chat.activities.views.voiceplayer.RecordingPlayerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;



public class GroupsMessageAdapters extends RecyclerView.Adapter<GroupsMessageAdapters.ViewHolder> {

    //그룹 메세지의 오른쪽은 숫자 0으로 왼쪽은 1로 설정하여 같은 위치가 아닌 서로 다른 위치에서
    //대화 형식으로 보이도록 했습니다.
    //채팅 리스트와 위치를 알려주는 Map, 오디오 플레이어, 녹음 플레이어 등이 보여지도록 했습니다.
    //만약 오디오 플레이어가 실행하지 않으면 녹음 플레이어도 실행하지 않도록
    //boolean(false or true)을 지정해주었습니다.

    private final int MSG_TYPE_RIGHT = 0;
    private final int MSG_TYPE_LEFT = 1;

    private final Context mContext;
    private final ArrayList<Chat> mChats;
    private final Map<String, User> userList;
    private final ArrayList<AudioPlayerView> myViewList;
    private final ArrayList<RecordingPlayerView> myRecList;
    private boolean isAudioPlaying = false, isRecordingPlaying = false;
    private final Screens screens;


    //유저 목록과 그룹 채팅 목록, 녹음 목록 등을 확인할 수 있습니다.
    public GroupsMessageAdapters(Context mContext, final ArrayList<Chat> usersList, final Map<String, User> userList) {
        this.mContext = mContext;
        this.mChats = usersList;
        this.userList = userList;
        this.myViewList = new ArrayList<>();
        this.myRecList = new ArrayList<>();
        screens = new Screens(mContext);
    }


    //뷰 타입이 오른쪽이면 오른쪽 말풍선이 보이며, 오른쪽 그룹 채팅 리스트가 뜨고,
    //뷰 타입이 왼쪽이면 왼쪽 말풍선이, 왼쪽 그룹 채팅 리스트 레이아웃이 보이도록 했습니다.

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.list_group_chat_right, viewGroup, false);
            return new GroupsMessageAdapters.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.list_group_chat_left, viewGroup, false);
            return new GroupsMessageAdapters.ViewHolder(view);
        }
    }

    @Override
    public void onViewRecycled(@NonNull @NotNull ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    private void showTextLayout(final ViewHolder viewHolder, final Chat chat) {
        try {
            viewHolder.txtShowMessage.setVisibility(View.VISIBLE);
            viewHolder.txtShowMessage.setText(chat.getMessage());
        } catch (Exception e) {
            Utils.getErrors(e);
        }
    }

    private void setImageLayout(final ViewHolder viewHolder, final Chat chat) {
        try {
            viewHolder.imgPath.setVisibility(View.VISIBLE);
            Utils.setChatImage(mContext, chat.getImgPath(), viewHolder.imgPath);
            viewHolder.imgPath.setOnClickListener(new SingleClickListener() {
                @Override
                public void onClickView(View v) {
                    screens.openFullImageViewActivity(v, chat.getImgPath(), "");
                }
            });
        } catch (Exception e) {
            Utils.getErrors(e);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {
        final Chat chat = mChats.get(position);
        final String attachType = chat.getAttachmentType();
        final User user = userList.get(chat.getSender());
        try {
            viewHolder.txtShowMessage.setVisibility(View.GONE);
            viewHolder.imgPath.setVisibility(View.GONE);

            viewHolder.recordingLayout.setVisibility(View.GONE);

            viewHolder.audioLayout.setVisibility(View.GONE);

            viewHolder.documentLayout.setVisibility(View.GONE);

            viewHolder.videoLayout.setVisibility(View.GONE);

            viewHolder.contactLayout.setVisibility(View.GONE);

            viewHolder.locationLayout.setVisibility(View.GONE);

        } catch (Exception e) {
            Utils.getErrors(e);
        }

        if (Utils.isEmpty(attachType)) {
            if (Utils.isEmpty(chat.getType())) { //사용자가 이전에 채팅한 내역들을 확인할 수 있습니다.
                showTextLayout(viewHolder, chat); //어느 유저와 채팅을 했었는지 유저의 사진과 메세지들을 확인합니다.
            } else {
                if (chat.getType().equalsIgnoreCase(TYPE_IMAGE)) {
                    setImageLayout(viewHolder, chat);
                } else {
                    showTextLayout(viewHolder, chat);
                }
            }
        } else {
            if (attachType.equalsIgnoreCase(TYPE_TEXT)) {
                showTextLayout(viewHolder, chat);
            } else if (attachType.equalsIgnoreCase(TYPE_IMAGE)) {
                setImageLayout(viewHolder, chat);
            } else if (attachType.equalsIgnoreCase(TYPE_RECORDING)) {
                try {
                    viewHolder.recordingLayout.setVisibility(View.VISIBLE);

                    switch (viewHolder.getItemViewType()) {
                        case MSG_TYPE_LEFT:
                            final String receiverPath = Utils.getReceiveDirectory(mContext, attachType) + SLASH + chat.getAttachmentFileName();
                            if (new File(receiverPath).exists()) {
                                viewHolder.recordingPlayerView.setAudio(receiverPath);
                                if (chat.getDownloadProgress() == COMPLETED) {
                                    viewHolder.recordingPlayerView.hidePlayProgressAndPlay();
                                } else {
                                    viewHolder.recordingPlayerView.hidePlayProgressbar();
                                }
                            } else {
                                viewHolder.recordingPlayerView.showDownloadButton();
                                viewHolder.recordingPlayerView.getImgDownload().setOnClickListener(new SingleClickListener() {
                                    @Override
                                    public void onClickView(View v) {
                                        viewHolder.recordingPlayerView.showPlayProgressbar();
                                        viewHolder.broadcastDownloadEvent(chat);
                                    }
                                });
                                viewHolder.recordingPlayerView.setAudio(null);  // 파일에 대한 기본 널 값 패스를 찾지 못했다는 메세지입니다.
                                try {
                                    viewHolder.recordingPlayerView.getTxtProcess().setText(Utils.getFileSize(chat.getAttachmentSize()));
                                } catch (Exception ignored) {
                                }
                            }
                            break;
                        case MSG_TYPE_RIGHT: // 로그인 한 사용자가 이미 전송 된 파일을 확인한 다음 /.sent/ 폴더 내부에서 확인합니다.
                            final String path = Utils.getSentDirectory(mContext, attachType) + SLASH + chat.getAttachmentFileName();
                            if (new File(path).exists()) {
                                viewHolder.recordingPlayerView.setAudio(path);
                            } else {
                                viewHolder.recordingPlayerView.setAudio(null); // 파일에 대한 기본 널 값 패스를 찾지 못했다는 메세지입니다.
                            }
                            break;
                    }

                    viewHolder.recordingPlayerView.getImgPlay().setOnClickListener(new SingleClickListener() {
                        @Override
                        public void onClickView(View v) {
                            isRecordingPlaying = true;
                            isAudioPlaying = false;
                            playingTrack(viewHolder);
                        }
                    });

                    viewHolder.recordingPlayerView.getImgPause().setOnClickListener(new SingleClickListener() {
                        @Override
                        public void onClickView(View v) {
                            isRecordingPlaying = false;
                            viewHolder.recordingPlayerView.getImgPauseClickListener().onClick(v);
                        }
                    });

                    viewHolder.recordingPlayerView.getSeekBar().setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            if (!Utils.isEmpty(viewHolder.recordingPlayerView.getPath())) {
                                viewHolder.recordingPlayerView.getSeekBarListener().onProgressChanged(seekBar, progress, fromUser);
                            }
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {
                            if (!Utils.isEmpty(viewHolder.recordingPlayerView.getPath())) {
                                viewHolder.recordingPlayerView.getSeekBarListener().onStartTrackingTouch(seekBar);
                            }
                            isRecordingPlaying = true;
                            isAudioPlaying = false;
                            playingTrack(viewHolder);
                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {
                            if (!Utils.isEmpty(viewHolder.recordingPlayerView.getPath())) {
                                viewHolder.recordingPlayerView.getSeekBarListener().onStopTrackingTouch(seekBar);
                            } else {
                                viewHolder.recordingPlayerView.getSeekBar().setProgress(0);
                            }
                        }
                    });

                    assert user != null;
                    if (!Utils.isEmpty(user.getImageURL())) {
                        Utils.setProfileImage(mContext, user.getImageURL(), viewHolder.recordingPlayerView.getVoiceUserImage());
                    }
                } catch (Exception e) {
                    Utils.getErrors(e);
                }
            } else if (attachType.equalsIgnoreCase(TYPE_AUDIO)) {
                try {
                    viewHolder.audioLayout.setVisibility(View.VISIBLE);

                    switch (viewHolder.getItemViewType()) {

                        case MSG_TYPE_LEFT:
                            final String receivePath = Utils.getReceiveDirectory(mContext, attachType) + SLASH + chat.getAttachmentFileName();
                            //Utils.sout("Audio LeftPath::: " + receivePath + " << " + new File(receivePath).exists());
                            if (new File(receivePath).exists()) {

                                viewHolder.audioPlayerView.setAudio(receivePath);

                                if (chat.getDownloadProgress() == COMPLETED) {
                                    viewHolder.audioPlayerView.hidePlayProgressAndPlay();
                                } else {
                                    viewHolder.audioPlayerView.hidePlayProgressbar();
                                }
                            } else {
                                viewHolder.audioPlayerView.showDownloadButton();
                                viewHolder.audioPlayerView.getImgDownload().setOnClickListener(new SingleClickListener() {
                                    @Override
                                    public void onClickView(View v) {
                                        viewHolder.audioPlayerView.showPlayProgressbar();
                                        viewHolder.broadcastDownloadEvent(chat);
                                    }
                                });
                                viewHolder.audioPlayerView.setAudio(null); // 파일에 대한 기본 널 값 패스를 찾지 못했다는 메세지입니다.
                                try {
                                    viewHolder.audioPlayerView.getTxtProcess().setText(Utils.getFileSize(chat.getAttachmentSize()));
                                } catch (Exception ignored) {
                                }
                            }
                            break;
                        case MSG_TYPE_RIGHT:
                            final String path = Utils.getSentDirectory(mContext, attachType) + SLASH + chat.getAttachmentFileName();
//                            Utils.sout("Audio Path right:: " + path + " >>>> " + new File(path).exists());
                            if (new File(path).exists()) {
                                viewHolder.audioPlayerView.setAudio(path);
                            } else {
                                viewHolder.audioPlayerView.setAudio(null); // 파일에 대한 기본 널 값 패스를 찾지 못했다는 메세지입니다.
                            }
                            break;
                    }
                    viewHolder.audioPlayerView.getImgPlay().setOnClickListener(new SingleClickListener() {
                        @Override
                        public void onClickView(View v) {
                            isAudioPlaying = true;
                            isRecordingPlaying = false;
                            playingTrack(viewHolder);
                        }
                    });
                    viewHolder.audioPlayerView.getImgPause().setOnClickListener(new SingleClickListener() {
                        @Override
                        public void onClickView(View v) {
                            isAudioPlaying = false;
                            viewHolder.audioPlayerView.getImgPauseClickListener().onClick(v);
                        }
                    });

                    viewHolder.audioPlayerView.getSeekBar().setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            if (!Utils.isEmpty(viewHolder.audioPlayerView.getPath())) {
                                viewHolder.audioPlayerView.getSeekBarListener().onProgressChanged(seekBar, progress, fromUser);
                            }
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {
                            if (!Utils.isEmpty(viewHolder.audioPlayerView.getPath())) {
                                viewHolder.audioPlayerView.getSeekBarListener().onStartTrackingTouch(seekBar);
                            }
                            isAudioPlaying = true;
                            isRecordingPlaying = false;
                            playingTrack(viewHolder);
                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {
                            if (!Utils.isEmpty(viewHolder.audioPlayerView.getPath())) {
                                viewHolder.audioPlayerView.getSeekBarListener().onStopTrackingTouch(seekBar);
                            } else {
                                viewHolder.audioPlayerView.getSeekBar().setProgress(0);
                            }
                        }
                    });
                    viewHolder.audioPlayerView.setFileName(chat.getAttachmentName());
                } catch (Exception ignored) {
                }
            } else if (attachType.equalsIgnoreCase(TYPE_DOCUMENT)) {
                try {
                    viewHolder.documentLayout.setVisibility(View.VISIBLE);
                    viewHolder.imgFileDownload.setVisibility(View.GONE);
                    viewHolder.fileProgressBar.setVisibility(View.GONE);
                    viewHolder.imgFileIcon.setVisibility(View.GONE);

                    switch (viewHolder.getItemViewType()) {
                        case MSG_TYPE_LEFT:
                            final String receivePath = Utils.getReceiveDirectory(mContext, attachType) + SLASH + chat.getAttachmentFileName();

                            if (new File(receivePath).exists()) {
                                viewHolder.imgFileIcon.setVisibility(View.VISIBLE);
                                viewHolder.imgFileDownload.setVisibility(View.GONE);
                                viewHolder.fileProgressBar.setVisibility(View.GONE);
                            } else {
                                viewHolder.imgFileIcon.setVisibility(View.GONE);
                                viewHolder.imgFileDownload.setVisibility(View.VISIBLE);
                                viewHolder.fileProgressBar.setVisibility(View.GONE);

                                viewHolder.imgFileDownload.setOnClickListener(new SingleClickListener() {
                                    @Override
                                    public void onClickView(View v) {
                                        viewHolder.imgFileDownload.setVisibility(View.GONE);
                                        viewHolder.fileProgressBar.setVisibility(View.VISIBLE);
                                        viewHolder.broadcastDownloadEvent(chat);
                                    }
                                });
                            }
                            break;

                        case MSG_TYPE_RIGHT:
                            viewHolder.imgFileIcon.setVisibility(View.VISIBLE);
                            break;
                    }
                    viewHolder.documentLayout.setOnClickListener(new SingleClickListener() {
                        @Override
                        public void onClickView(View v) {
                            switch (viewHolder.getItemViewType()) {
                                case MSG_TYPE_LEFT:
                                    try {
                                        final String receivePath = Utils.getReceiveDirectory(mContext, attachType) + SLASH + chat.getAttachmentFileName();
                                        if (new File(receivePath).exists()) {
                                            mContext.startActivity(Utils.getOpenFileIntent(mContext, receivePath));
                                        }
                                    } catch (Exception e) {
                                        Utils.getErrors(e);
                                    }
                                    break;
                                case MSG_TYPE_RIGHT:
                                    try {
                                        final String path = Utils.getSentDirectory(mContext, attachType) + SLASH + chat.getAttachmentFileName();
                                        mContext.startActivity(Utils.getOpenFileIntent(mContext, path));
                                    } catch (Exception e) {
                                        Utils.getErrors(e);
                                        screens.showToast("파일을 찾을 수 없습니다!");
                                    }
                                    break;
                            }
                        }
                    });
                    viewHolder.txtFileName.setText(chat.getAttachmentName());
                    viewHolder.txtFileExt.setText(Utils.getFileExtensionFromPath(chat.getAttachmentFileName()).toUpperCase());
                    viewHolder.txtFileSize.setText(Utils.getFileSize(chat.getAttachmentSize()));
                } catch (Exception e) {
                    Utils.getErrors(e);
                }
            } else if (attachType.equalsIgnoreCase(TYPE_VIDEO)) {
                try {
                    viewHolder.videoLayout.setVisibility(View.VISIBLE);
                    viewHolder.imgVideoPlay.setVisibility(View.GONE);
                    viewHolder.videoProgressBar.setVisibility(View.GONE);

                    switch (viewHolder.getItemViewType()) {
                        case MSG_TYPE_LEFT:
                            try {
                                final File receivePath = new File(Utils.getReceiveDirectory(mContext, attachType) + SLASH + chat.getAttachmentFileName());
                                if (receivePath.exists()) {
                                    viewHolder.imgVideoPlay.setVisibility(View.VISIBLE);
                                    viewHolder.imgVideoDownload.setVisibility(View.GONE);
                                    viewHolder.videoProgressBar.setVisibility(View.GONE);
                                } else {
                                    viewHolder.imgVideoPlay.setVisibility(View.GONE);
                                    viewHolder.imgVideoDownload.setVisibility(View.VISIBLE);
                                    viewHolder.videoProgressBar.setVisibility(View.GONE);

                                    viewHolder.imgVideoDownload.setOnClickListener(new SingleClickListener() {
                                        @Override
                                        public void onClickView(View v) {
                                            viewHolder.imgVideoDownload.setVisibility(View.GONE);
                                            viewHolder.videoProgressBar.setVisibility(View.VISIBLE);
                                            viewHolder.broadcastDownloadEvent(chat);
                                        }
                                    });
                                }
                            } catch (Exception ignored) {
                            }
                            break;
                        case MSG_TYPE_RIGHT:
                            try {
                                viewHolder.imgVideoPlay.setVisibility(View.VISIBLE);
                                viewHolder.imgVideoDownload.setVisibility(View.GONE);
                                viewHolder.videoProgressBar.setVisibility(View.GONE);
                            } catch (Exception ignored) {
                            }
                            break;
                    }

                    viewHolder.imgVideoPlay.setOnClickListener(new SingleClickListener() {
                        @Override
                        public void onClickView(View v) {
                            switch (viewHolder.getItemViewType()) {
                                case MSG_TYPE_LEFT:
                                    try {
                                        final String receivePath = Utils.getReceiveDirectory(mContext, attachType) + SLASH + chat.getAttachmentFileName();
                                        if (new File(receivePath).exists()) {
                                            Utils.openPlayingVideo(mContext, new File(receivePath));
                                        }
                                    } catch (Exception e) {
                                        Utils.getErrors(e);
                                    }
                                    break;
                                case MSG_TYPE_RIGHT:
                                    try {
                                        final File path = new File(Utils.getSentDirectory(mContext, attachType) + SLASH + chat.getAttachmentFileName());
                                        Utils.openPlayingVideo(mContext, path);
                                    } catch (Exception e) {
                                        Utils.getErrors(e);
                                    }
                                    break;
                            }
                        }
                    });

                    viewHolder.txtVideoDuration.setText(chat.getAttachmentDuration());
                    viewHolder.txtVideoSize.setText(Utils.getFileSize(chat.getAttachmentSize()));
                    Utils.setChatImage(mContext, chat.getAttachmentData(), viewHolder.videoThumbnail);
                } catch (Exception e) {
                    Utils.getErrors(e);
                }
            } else if (attachType.equalsIgnoreCase(TYPE_CONTACT)) {
                try {
                    viewHolder.contactLayout.setVisibility(View.VISIBLE);
                    viewHolder.txtContactName.setText(chat.getAttachmentFileName());
                    viewHolder.btnMessageContact.setOnClickListener(new SingleClickListener() {
                        @Override
                        public void onClickView(View v) {
                            Utils.shareApp((Activity) mContext);
                        }
                    });
                    viewHolder.contactLayout.setOnClickListener(new SingleClickListener() {
                        @Override
                        public void onClickView(View v) {
                            Utils.openCallIntent(mContext, chat.getAttachmentDuration());
                        }
                    });
                } catch (Exception e) {
                    Utils.getErrors(e);
                }
            } else if (attachType.equalsIgnoreCase(TYPE_LOCATION)) {
                try {
                    viewHolder.locationLayout.setVisibility(View.VISIBLE);
                    LocationAddress locationAddress = new Gson().fromJson(chat.getAttachmentData(), LocationAddress.class);
                    int topLeft = 0, topRight = 0;
                    switch (viewHolder.getItemViewType()) {
                        case MSG_TYPE_LEFT:
                            topRight = 16;
                            break;
                        case MSG_TYPE_RIGHT:
                            topLeft = 16;
                            break;
                    }
                    Utils.showStaticMap(mContext, locationAddress, topLeft, topRight, viewHolder.imgLocation);
                    if (Utils.isEmpty(locationAddress.getName())) {
                        viewHolder.txtLocationName.setVisibility(View.GONE);
                    } else {
                        viewHolder.txtLocationName.setVisibility(View.VISIBLE);
                        viewHolder.txtLocationName.setText(locationAddress.getName());
                    }
                    viewHolder.txtAddress.setText(locationAddress.getAddress());
                    viewHolder.locationLayout.setOnClickListener(new SingleClickListener() {
                        @Override
                        public void onClickView(View v) {
                            Utils.openMapWithAddress(mContext, locationAddress);
                        }
                    });
                } catch (Exception e) {
                    Utils.getErrors(e);
                }
            } else {
                try {
                    viewHolder.txtShowMessage.setVisibility(View.VISIBLE);
                    viewHolder.txtShowMessage.setText(chat.getMessage());
                } catch (Exception e) {
                    Utils.getErrors(e);
                }
            }
        }

        viewHolder.txtOnlyDate.setVisibility(View.GONE);
        try {
            final long first = Utils.dateToMillis(mChats.get(position - 1).getDatetime());
            final long second = Utils.dateToMillis(chat.getDatetime());
            if (!Utils.hasSameDate(first, second)) {
                viewHolder.txtOnlyDate.setVisibility(View.VISIBLE);
                viewHolder.txtOnlyDate.setText(Utils.formatFullDate(chat.getDatetime()));
            }
        } catch (Exception e) {
            if (position == 0) {
                viewHolder.txtOnlyDate.setVisibility(View.VISIBLE);
                viewHolder.txtOnlyDate.setText(Utils.formatFullDate(chat.getDatetime()));
            }
        }

        switch (viewHolder.getItemViewType()) {
            case MSG_TYPE_LEFT:
                try {
                    assert user != null;
                    viewHolder.txtName.setText(user.getUsername());
                } catch (Exception e) {
                    Utils.getErrors(e);
                }
                break;
            case MSG_TYPE_RIGHT:
                break;
        }

        long timeMilliSeconds = 0;
        try {
            timeMilliSeconds = Utils.dateToMillis(chat.getDatetime());
        } catch (Exception ignored) {
        }

        if (timeMilliSeconds > 0) {
            viewHolder.txtMsgTime.setText(Utils.formatLocalTime(timeMilliSeconds));
        } else {
            viewHolder.txtMsgTime.setVisibility(View.GONE);
        }

        viewHolder.imgMsgSeen.setVisibility(View.GONE);

    }

    public void playingTrack(final ViewHolder viewHolder) {
        try {
            if (isAudioPlaying) {
                if (Utils.isEmpty(viewHolder.audioPlayerView.getPath())) {
                    viewHolder.audioPlayerView.getImgPlayNoFileClickListener().onClick(viewHolder.audioPlayerView);
                } else {
                    if (myViewList.isEmpty()) {
                        myViewList.add(viewHolder.audioPlayerView);
                        viewHolder.audioPlayerView.getImgPlayClickListener().onClick(viewHolder.audioPlayerView);
                    } else {// 오디오 중 하나가 이미 재생 될 때 전화를 걸므로 먼저 잠시 멈추고 새 오디오 트랙을 재생합니다.
                        final AudioPlayerView oldPlayerView = myViewList.get(ZERO);
                        oldPlayerView.getImgPauseClickListener().onClick(viewHolder.audioPlayerView);
                        myViewList.remove(ZERO);
                        viewHolder.audioPlayerView.getImgPlay().callOnClick();
                    }
                }
            } else {// 오디오가 이미 재생되고 녹음 트랙을 재생하려고 할 때 호출하므로 오디오 트랙을 먼저 일시 중지하세요.
                if (!myViewList.isEmpty()) {
                    final AudioPlayerView oldPlayerView = myViewList.get(ZERO);
                    oldPlayerView.getImgPauseClickListener().onClick(viewHolder.audioPlayerView);
                    myViewList.remove(ZERO);
                }
            }
        } catch (Exception e) {
            Utils.getErrors(e);
        }

        try {
            if (isRecordingPlaying) {
                if (Utils.isEmpty(viewHolder.recordingPlayerView.getPath())) {
                    viewHolder.recordingPlayerView.getImgPlayNoFileClickListener().onClick(viewHolder.recordingPlayerView);
                } else {
                    if (myRecList.isEmpty()) {
                        myRecList.add(viewHolder.recordingPlayerView);
                        viewHolder.recordingPlayerView.getImgPlayClickListener().onClick(viewHolder.recordingPlayerView);
                    } else {// 레코드가 이미 재생 될 때 전화를 걸므로 먼저 잠시 멈추고 새로운 녹음 트랙을 재생합니다.
                        final RecordingPlayerView oldPlayerView = myRecList.get(ZERO);
                        oldPlayerView.getImgPauseClickListener().onClick(viewHolder.recordingPlayerView);
                        myRecList.remove(ZERO);
                        viewHolder.recordingPlayerView.getImgPlay().callOnClick();
                    }
                }
            } else {// 이미 재생하고 오디오 트랙을 재생하려고 할 때 전화를 걸므로 레코딩 트랙을 먼저 일시 중지하세요!
                if (!myRecList.isEmpty()) {
                    final RecordingPlayerView oldPlayerView = myRecList.get(ZERO);
                    oldPlayerView.getImgPauseClickListener().onClick(viewHolder.recordingPlayerView);
                    myRecList.remove(ZERO);
                }
            }

        } catch (Exception e) {
            Utils.getErrors(e);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView txtName;
        public final TextView txtShowMessage;
        public final TextView txtMsgTime;
        public final ImageView imgMsgSeen;
        public final TextView txtOnlyDate;
        public final ImageView imgPath;

        //New Component
        public RelativeLayout recordingLayout, audioLayout, documentLayout, videoLayout, contactLayout, locationLayout;
        public RecordingPlayerView recordingPlayerView;
        public AudioPlayerView audioPlayerView;
        public TextView txtFileName, txtFileSize, txtFileExt, txtVideoDuration, txtVideoSize, txtContactName, txtLocationName, txtAddress;
        public ImageView imgFileIcon, imgFileDownload, videoThumbnail, imgVideoPlay, imgVideoDownload, imgUserContact, imgLocation;
        public ProgressBar fileProgressBar, videoProgressBar;
        public Button btnMessageContact;


        //현재 날짜, 메세지 보기, 유저 이름, 이미지 메세지 씬, 문자 작성 날짜, 이미지 패치 등이 보여집니다.
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtOnlyDate = itemView.findViewById(R.id.txtOnlyDate);
            txtShowMessage = itemView.findViewById(R.id.txtShowMessage);
            txtName = itemView.findViewById(R.id.txtName);
            imgMsgSeen = itemView.findViewById(R.id.imgMsgSeen);
            txtMsgTime = itemView.findViewById(R.id.txtMsgTime);
            imgPath = itemView.findViewById(R.id.imgPath);
            try {
                recordingLayout = itemView.findViewById(R.id.recordingLayout);
                recordingPlayerView = itemView.findViewById(R.id.voicePlayerView);

                audioLayout = itemView.findViewById(R.id.audioLayout);
                audioPlayerView = itemView.findViewById(R.id.audioPlayerView);

                documentLayout = itemView.findViewById(R.id.documentLayout);
                txtFileName = itemView.findViewById(R.id.txtFileName);
                txtFileSize = itemView.findViewById(R.id.txtFileSize);
                txtFileExt = itemView.findViewById(R.id.txtFileExt);
                imgFileIcon = itemView.findViewById(R.id.imgFileIcon);
                imgFileDownload = itemView.findViewById(R.id.imgFileDownload);
                fileProgressBar = itemView.findViewById(R.id.fileProgressBar);

                videoLayout = itemView.findViewById(R.id.videoLayout);
                videoThumbnail = itemView.findViewById(R.id.videoThumbnail);
                imgVideoPlay = itemView.findViewById(R.id.imgVideoPlay);
                imgVideoDownload = itemView.findViewById(R.id.imgVideoDownload);
                videoProgressBar = itemView.findViewById(R.id.videoProgressBar);
                txtVideoDuration = itemView.findViewById(R.id.txtVideoDuration);
                txtVideoSize = itemView.findViewById(R.id.txtVideoSize);

                contactLayout = itemView.findViewById(R.id.contactLayout);
                imgUserContact = itemView.findViewById(R.id.imgUserContact);
                txtContactName = itemView.findViewById(R.id.txtContactName);
                btnMessageContact = itemView.findViewById(R.id.btnMessageContact);

                locationLayout = itemView.findViewById(R.id.locationLayout);
                imgLocation = itemView.findViewById(R.id.imgLocation);
                txtLocationName = itemView.findViewById(R.id.txtLocationName);
                txtAddress = itemView.findViewById(R.id.txtAddress);
            } catch (Exception e) {
                Utils.getErrors(e);
            }
        }

        private void broadcastDownloadEvent(Chat chat) {
            final Intent intent = new Intent(BROADCAST_DOWNLOAD_EVENT);
            intent.putExtra(DOWNLOAD_DATA, new DownloadFileEvent(chat, getAbsoluteAdapterPosition()));
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
        }
    }

    public void stopAudioFile() {
        if (!Utils.isEmpty(myViewList)) {
            try {
                for (int i = 0; i < myViewList.size(); i++) {
                    final AudioPlayerView audioPlayerView = myViewList.get(ZERO);
                    audioPlayerView.getImgPause().callOnClick();
                }
                myViewList.clear();
            } catch (Exception e) {
                Utils.getErrors(e);
            }
        }

        if (!Utils.isEmpty(myRecList)) {
            try {
                for (int i = 0; i < myRecList.size(); i++) {
                    final RecordingPlayerView recordingPlayerView = myRecList.get(ZERO);
                    recordingPlayerView.getImgPause().callOnClick();
                }
                myRecList.clear();
            } catch (Exception e) {
                Utils.getErrors(e);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mChats.size();
    }

    @Override
    public int getItemViewType(int position) {
        final Chat chat = mChats.get(position);
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        assert firebaseUser != null;
        if (chat.getSender().equalsIgnoreCase(firebaseUser.getUid())) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }
}
