package tv.danmaku.ijk.media.player.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import java.io.IOException;
import java.util.List;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IMediaPlayer.OnBufferingUpdateListener;
import tv.danmaku.ijk.media.player.IMediaPlayer.OnCompletionListener;
import tv.danmaku.ijk.media.player.IMediaPlayer.OnErrorListener;
import tv.danmaku.ijk.media.player.IMediaPlayer.OnInfoListener;
import tv.danmaku.ijk.media.player.IMediaPlayer.OnPreparedListener;
import tv.danmaku.ijk.media.player.IMediaPlayer.OnSeekCompleteListener;
import tv.danmaku.ijk.media.player.IMediaPlayer.OnVideoSizeChangedListener;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.widget.MediaController.MediaPlayerControl;

public class VideoView extends SurfaceView implements MediaPlayerControl {
    private static final int STATE_ERROR = -1;
    private static final int STATE_IDLE = 0;
    private static final int STATE_PAUSED = 4;
    private static final int STATE_PLAYBACK_COMPLETED = 5;
    private static final int STATE_PLAYING = 3;
    private static final int STATE_PREPARED = 2;
    private static final int STATE_PREPARING = 1;
    private static final int STATE_RESUME = 7;
    private static final int STATE_SUSPEND = 6;
    private static final int STATE_SUSPEND_UNSUPPORTED = 8;
    private static final String TAG = VideoView.class.getName();
    public static final int VIDEO_LAYOUT_ORIGIN = 0;
    public static final int VIDEO_LAYOUT_SCALE = 1;
    public static final int VIDEO_LAYOUT_STRETCH = 2;
    public static final int VIDEO_LAYOUT_ZOOM = 3;
    private int mActualVideoHeight;
    private OnBufferingUpdateListener mBufferingUpdateListener;
    private boolean mCanPause;
    private boolean mCanSeekBack;
    private boolean mCanSeekForward;
    private OnCompletionListener mCompletionListener;
    private Context mContext;
    private int mCurrentBufferPercentage;
    private int mCurrentState;
    private long mDuration;
    private OnErrorListener mErrorListener;
    private OnInfoListener mInfoListener;
    private View mMediaBufferingIndicator;
    private MediaController mMediaController;
    private IMediaPlayer mMediaPlayer;
    private OnBufferingUpdateListener mOnBufferingUpdateListener;
    private OnCompletionListener mOnCompletionListener;
    private OnErrorListener mOnErrorListener;
    private OnInfoListener mOnInfoListener;
    private OnPreparedListener mOnPreparedListener;
    private OnSeekCompleteListener mOnSeekCompleteListener;
    OnPreparedListener mPreparedListener;
    Callback mSHCallback;
    private OnSeekCompleteListener mSeekCompleteListener;
    private long mSeekWhenPrepared;
    OnVideoSizeChangedListener mSizeChangedListener;
    private int mSurfaceHeight;
    private SurfaceHolder mSurfaceHolder;
    private int mSurfaceWidth;
    private int mTargetState;
    private Uri mUri;
    private String mUserAgent;
    private int mVideoHeight;
    private int mVideoLayout;
    private int mVideoSarDen;
    private int mVideoSarNum;
    private int mVideoWidth;

    public void setActualVideoHeight(int h) {
        this.mActualVideoHeight = h;
    }

    public VideoView(Context context) {
        super(context);
        this.mCurrentState = 0;
        this.mTargetState = 0;
        this.mVideoLayout = 1;
        this.mSurfaceHolder = null;
        this.mMediaPlayer = null;
        this.mCanPause = true;
        this.mCanSeekBack = true;
        this.mCanSeekForward = true;
        this.mActualVideoHeight = 0;
        this.mSizeChangedListener = new OnVideoSizeChangedListener() {
            public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sarNum, int sarDen) {
                DebugLog.dfmt(VideoView.TAG, "onVideoSizeChanged: (%dx%d)", Integer.valueOf(width), Integer.valueOf(height));
                VideoView.this.mVideoWidth = mp.getVideoWidth();
                VideoView.this.mVideoHeight = mp.getVideoHeight();
                VideoView.this.mVideoSarNum = sarNum;
                VideoView.this.mVideoSarDen = sarDen;
                if (VideoView.this.mVideoWidth != 0 && VideoView.this.mVideoHeight != 0) {
                    VideoView.this.setVideoLayout(VideoView.this.mVideoLayout);
                }
            }
        };
        this.mPreparedListener = new OnPreparedListener() {
            public void onPrepared(IMediaPlayer mp) {
                DebugLog.d(VideoView.TAG, "onPrepared");
                VideoView.this.mCurrentState = 2;
                VideoView.this.mTargetState = 3;
                if (VideoView.this.mOnPreparedListener != null) {
                    VideoView.this.mOnPreparedListener.onPrepared(VideoView.this.mMediaPlayer);
                }
                if (VideoView.this.mMediaController != null) {
                    VideoView.this.mMediaController.setEnabled(true);
                }
                VideoView.this.mVideoWidth = mp.getVideoWidth();
                VideoView.this.mVideoHeight = mp.getVideoHeight();
                long seekToPosition = VideoView.this.mSeekWhenPrepared;
                if (seekToPosition != 0) {
                    VideoView.this.seekTo(seekToPosition);
                }
                if (VideoView.this.mVideoWidth != 0 && VideoView.this.mVideoHeight != 0) {
                    VideoView.this.setVideoLayout(VideoView.this.mVideoLayout);
                    if (VideoView.this.mSurfaceWidth != VideoView.this.mVideoWidth || VideoView.this.mSurfaceHeight != VideoView.this.mVideoHeight) {
                        return;
                    }
                    if (VideoView.this.mTargetState == 3) {
                        VideoView.this.start();
                        if (VideoView.this.mMediaController != null) {
                            VideoView.this.mMediaController.show();
                        }
                    } else if (!VideoView.this.isPlaying()) {
                        if ((seekToPosition != 0 || VideoView.this.getCurrentPosition() > 0) && VideoView.this.mMediaController != null) {
                            VideoView.this.mMediaController.show(0);
                        }
                    }
                } else if (VideoView.this.mTargetState == 3) {
                    VideoView.this.start();
                }
            }
        };
        this.mCompletionListener = new OnCompletionListener() {
            public void onCompletion(IMediaPlayer mp) {
                DebugLog.d(VideoView.TAG, "onCompletion");
                VideoView.this.mCurrentState = 5;
                VideoView.this.mTargetState = 5;
                if (VideoView.this.mMediaController != null) {
                    VideoView.this.mMediaController.hide();
                }
                if (VideoView.this.mOnCompletionListener != null) {
                    VideoView.this.mOnCompletionListener.onCompletion(VideoView.this.mMediaPlayer);
                }
            }
        };
        this.mErrorListener = new OnErrorListener() {
            public boolean onError(IMediaPlayer mp, int framework_err, int impl_err) {
                DebugLog.dfmt(VideoView.TAG, "Error: %d, %d", Integer.valueOf(framework_err), Integer.valueOf(impl_err));
                VideoView.this.mCurrentState = -1;
                VideoView.this.mTargetState = -1;
                if (VideoView.this.mMediaController != null) {
                    VideoView.this.mMediaController.hide();
                }
                if ((VideoView.this.mOnErrorListener == null || !VideoView.this.mOnErrorListener.onError(VideoView.this.mMediaPlayer, framework_err, impl_err)) && VideoView.this.mMediaBufferingIndicator != null) {
                    VideoView.this.mMediaBufferingIndicator.setVisibility(8);
                }
                return true;
            }
        };
        this.mBufferingUpdateListener = new OnBufferingUpdateListener() {
            public void onBufferingUpdate(IMediaPlayer mp, int percent) {
                VideoView.this.mCurrentBufferPercentage = percent;
                if (VideoView.this.mOnBufferingUpdateListener != null) {
                    VideoView.this.mOnBufferingUpdateListener.onBufferingUpdate(mp, percent);
                }
            }
        };
        this.mInfoListener = new OnInfoListener() {
            public boolean onInfo(IMediaPlayer mp, int what, int extra) {
                DebugLog.dfmt(VideoView.TAG, "onInfo: (%d, %d)", Integer.valueOf(what), Integer.valueOf(extra));
                if (VideoView.this.mOnInfoListener != null) {
                    VideoView.this.mOnInfoListener.onInfo(mp, what, extra);
                } else if (VideoView.this.mMediaPlayer != null) {
                    if (what == IMediaPlayer.MEDIA_INFO_BUFFERING_START) {
                        DebugLog.dfmt(VideoView.TAG, "onInfo: (MEDIA_INFO_BUFFERING_START)", new Object[0]);
                        if (VideoView.this.mMediaBufferingIndicator != null) {
                            VideoView.this.mMediaBufferingIndicator.setVisibility(0);
                        }
                    } else if (what == IMediaPlayer.MEDIA_INFO_BUFFERING_END) {
                        DebugLog.dfmt(VideoView.TAG, "onInfo: (MEDIA_INFO_BUFFERING_END)", new Object[0]);
                        if (VideoView.this.mMediaBufferingIndicator != null) {
                            VideoView.this.mMediaBufferingIndicator.setVisibility(8);
                        }
                    }
                }
                return true;
            }
        };
        this.mSeekCompleteListener = new OnSeekCompleteListener() {
            public void onSeekComplete(IMediaPlayer mp) {
                DebugLog.d(VideoView.TAG, "onSeekComplete");
                if (VideoView.this.mOnSeekCompleteListener != null) {
                    VideoView.this.mOnSeekCompleteListener.onSeekComplete(mp);
                }
            }
        };
        this.mSHCallback = new Callback() {
            public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
                VideoView.this.mSurfaceHolder = holder;
                if (VideoView.this.mMediaPlayer != null) {
                    VideoView.this.mMediaPlayer.setDisplay(VideoView.this.mSurfaceHolder);
                }
                VideoView.this.mSurfaceWidth = w;
                VideoView.this.mSurfaceHeight = h;
                boolean isValidState;
                if (VideoView.this.mTargetState == 3) {
                    isValidState = true;
                } else {
                    isValidState = false;
                }
                boolean hasValidSize;
                if (VideoView.this.mVideoWidth == w && VideoView.this.mVideoHeight == h) {
                    hasValidSize = true;
                } else {
                    hasValidSize = false;
                }
                if (VideoView.this.mMediaPlayer != null && isValidState && hasValidSize) {
                    if (VideoView.this.mSeekWhenPrepared != 0) {
                        VideoView.this.seekTo(VideoView.this.mSeekWhenPrepared);
                    }
                    VideoView.this.start();
                    if (VideoView.this.mMediaController != null) {
                        if (VideoView.this.mMediaController.isShowing()) {
                            VideoView.this.mMediaController.hide();
                        }
                        VideoView.this.mMediaController.show();
                    }
                }
            }

            public void surfaceCreated(SurfaceHolder holder) {
                VideoView.this.mSurfaceHolder = holder;
                if (VideoView.this.mMediaPlayer != null && VideoView.this.mCurrentState == 6 && VideoView.this.mTargetState == 7) {
                    VideoView.this.mMediaPlayer.setDisplay(VideoView.this.mSurfaceHolder);
                    VideoView.this.resume();
                    return;
                }
                VideoView.this.openVideo();
            }

            public void surfaceDestroyed(SurfaceHolder holder) {
                VideoView.this.mSurfaceHolder = null;
                if (VideoView.this.mMediaController != null) {
                    VideoView.this.mMediaController.hide();
                }
                if (VideoView.this.mCurrentState != 6) {
                    VideoView.this.release(true);
                }
            }
        };
        initVideoView(context);
    }

    public VideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mCurrentState = 0;
        this.mTargetState = 0;
        this.mVideoLayout = 1;
        this.mSurfaceHolder = null;
        this.mMediaPlayer = null;
        this.mCanPause = true;
        this.mCanSeekBack = true;
        this.mCanSeekForward = true;
        this.mActualVideoHeight = 0;
        this.mSizeChangedListener = new OnVideoSizeChangedListener() {
            public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sarNum, int sarDen) {
                DebugLog.dfmt(VideoView.TAG, "onVideoSizeChanged: (%dx%d)", Integer.valueOf(width), Integer.valueOf(height));
                VideoView.this.mVideoWidth = mp.getVideoWidth();
                VideoView.this.mVideoHeight = mp.getVideoHeight();
                VideoView.this.mVideoSarNum = sarNum;
                VideoView.this.mVideoSarDen = sarDen;
                if (VideoView.this.mVideoWidth != 0 && VideoView.this.mVideoHeight != 0) {
                    VideoView.this.setVideoLayout(VideoView.this.mVideoLayout);
                }
            }
        };
        this.mPreparedListener = new OnPreparedListener() {
            public void onPrepared(IMediaPlayer mp) {
                DebugLog.d(VideoView.TAG, "onPrepared");
                VideoView.this.mCurrentState = 2;
                VideoView.this.mTargetState = 3;
                if (VideoView.this.mOnPreparedListener != null) {
                    VideoView.this.mOnPreparedListener.onPrepared(VideoView.this.mMediaPlayer);
                }
                if (VideoView.this.mMediaController != null) {
                    VideoView.this.mMediaController.setEnabled(true);
                }
                VideoView.this.mVideoWidth = mp.getVideoWidth();
                VideoView.this.mVideoHeight = mp.getVideoHeight();
                long seekToPosition = VideoView.this.mSeekWhenPrepared;
                if (seekToPosition != 0) {
                    VideoView.this.seekTo(seekToPosition);
                }
                if (VideoView.this.mVideoWidth != 0 && VideoView.this.mVideoHeight != 0) {
                    VideoView.this.setVideoLayout(VideoView.this.mVideoLayout);
                    if (VideoView.this.mSurfaceWidth != VideoView.this.mVideoWidth || VideoView.this.mSurfaceHeight != VideoView.this.mVideoHeight) {
                        return;
                    }
                    if (VideoView.this.mTargetState == 3) {
                        VideoView.this.start();
                        if (VideoView.this.mMediaController != null) {
                            VideoView.this.mMediaController.show();
                        }
                    } else if (!VideoView.this.isPlaying()) {
                        if ((seekToPosition != 0 || VideoView.this.getCurrentPosition() > 0) && VideoView.this.mMediaController != null) {
                            VideoView.this.mMediaController.show(0);
                        }
                    }
                } else if (VideoView.this.mTargetState == 3) {
                    VideoView.this.start();
                }
            }
        };
        this.mCompletionListener = new OnCompletionListener() {
            public void onCompletion(IMediaPlayer mp) {
                DebugLog.d(VideoView.TAG, "onCompletion");
                VideoView.this.mCurrentState = 5;
                VideoView.this.mTargetState = 5;
                if (VideoView.this.mMediaController != null) {
                    VideoView.this.mMediaController.hide();
                }
                if (VideoView.this.mOnCompletionListener != null) {
                    VideoView.this.mOnCompletionListener.onCompletion(VideoView.this.mMediaPlayer);
                }
            }
        };
        this.mErrorListener = new OnErrorListener() {
            public boolean onError(IMediaPlayer mp, int framework_err, int impl_err) {
                DebugLog.dfmt(VideoView.TAG, "Error: %d, %d", Integer.valueOf(framework_err), Integer.valueOf(impl_err));
                VideoView.this.mCurrentState = -1;
                VideoView.this.mTargetState = -1;
                if (VideoView.this.mMediaController != null) {
                    VideoView.this.mMediaController.hide();
                }
                if ((VideoView.this.mOnErrorListener == null || !VideoView.this.mOnErrorListener.onError(VideoView.this.mMediaPlayer, framework_err, impl_err)) && VideoView.this.mMediaBufferingIndicator != null) {
                    VideoView.this.mMediaBufferingIndicator.setVisibility(8);
                }
                return true;
            }
        };
        this.mBufferingUpdateListener = new OnBufferingUpdateListener() {
            public void onBufferingUpdate(IMediaPlayer mp, int percent) {
                VideoView.this.mCurrentBufferPercentage = percent;
                if (VideoView.this.mOnBufferingUpdateListener != null) {
                    VideoView.this.mOnBufferingUpdateListener.onBufferingUpdate(mp, percent);
                }
            }
        };
        this.mInfoListener = new OnInfoListener() {
            public boolean onInfo(IMediaPlayer mp, int what, int extra) {
                DebugLog.dfmt(VideoView.TAG, "onInfo: (%d, %d)", Integer.valueOf(what), Integer.valueOf(extra));
                if (VideoView.this.mOnInfoListener != null) {
                    VideoView.this.mOnInfoListener.onInfo(mp, what, extra);
                } else if (VideoView.this.mMediaPlayer != null) {
                    if (what == IMediaPlayer.MEDIA_INFO_BUFFERING_START) {
                        DebugLog.dfmt(VideoView.TAG, "onInfo: (MEDIA_INFO_BUFFERING_START)", new Object[0]);
                        if (VideoView.this.mMediaBufferingIndicator != null) {
                            VideoView.this.mMediaBufferingIndicator.setVisibility(0);
                        }
                    } else if (what == IMediaPlayer.MEDIA_INFO_BUFFERING_END) {
                        DebugLog.dfmt(VideoView.TAG, "onInfo: (MEDIA_INFO_BUFFERING_END)", new Object[0]);
                        if (VideoView.this.mMediaBufferingIndicator != null) {
                            VideoView.this.mMediaBufferingIndicator.setVisibility(8);
                        }
                    }
                }
                return true;
            }
        };
        this.mSeekCompleteListener = new OnSeekCompleteListener() {
            public void onSeekComplete(IMediaPlayer mp) {
                DebugLog.d(VideoView.TAG, "onSeekComplete");
                if (VideoView.this.mOnSeekCompleteListener != null) {
                    VideoView.this.mOnSeekCompleteListener.onSeekComplete(mp);
                }
            }
        };
        this.mSHCallback = new Callback() {
            public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
                VideoView.this.mSurfaceHolder = holder;
                if (VideoView.this.mMediaPlayer != null) {
                    VideoView.this.mMediaPlayer.setDisplay(VideoView.this.mSurfaceHolder);
                }
                VideoView.this.mSurfaceWidth = w;
                VideoView.this.mSurfaceHeight = h;
                boolean isValidState;
                if (VideoView.this.mTargetState == 3) {
                    isValidState = true;
                } else {
                    isValidState = false;
                }
                boolean hasValidSize;
                if (VideoView.this.mVideoWidth == w && VideoView.this.mVideoHeight == h) {
                    hasValidSize = true;
                } else {
                    hasValidSize = false;
                }
                if (VideoView.this.mMediaPlayer != null && isValidState && hasValidSize) {
                    if (VideoView.this.mSeekWhenPrepared != 0) {
                        VideoView.this.seekTo(VideoView.this.mSeekWhenPrepared);
                    }
                    VideoView.this.start();
                    if (VideoView.this.mMediaController != null) {
                        if (VideoView.this.mMediaController.isShowing()) {
                            VideoView.this.mMediaController.hide();
                        }
                        VideoView.this.mMediaController.show();
                    }
                }
            }

            public void surfaceCreated(SurfaceHolder holder) {
                VideoView.this.mSurfaceHolder = holder;
                if (VideoView.this.mMediaPlayer != null && VideoView.this.mCurrentState == 6 && VideoView.this.mTargetState == 7) {
                    VideoView.this.mMediaPlayer.setDisplay(VideoView.this.mSurfaceHolder);
                    VideoView.this.resume();
                    return;
                }
                VideoView.this.openVideo();
            }

            public void surfaceDestroyed(SurfaceHolder holder) {
                VideoView.this.mSurfaceHolder = null;
                if (VideoView.this.mMediaController != null) {
                    VideoView.this.mMediaController.hide();
                }
                if (VideoView.this.mCurrentState != 6) {
                    VideoView.this.release(true);
                }
            }
        };
        initVideoView(context);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(this.mVideoWidth, widthMeasureSpec), getDefaultSize(this.mVideoHeight, heightMeasureSpec));
    }

    public void setVideoLayout(int layout) {
        LayoutParams lp = getLayoutParams();
        Pair<Integer, Integer> res = ScreenResolution.getResolution(this.mContext);
        int windowWidth = ((Integer) res.first).intValue();
        int windowHeight = ((Integer) res.second).intValue();
        if (windowWidth < windowHeight && this.mActualVideoHeight > 0) {
            windowHeight = this.mActualVideoHeight;
        }
        float windowRatio = ((float) windowWidth) / ((float) windowHeight);
        int sarNum = this.mVideoSarNum;
        int sarDen = this.mVideoSarDen;
        if (this.mVideoHeight > 0 && this.mVideoWidth > 0) {
            float videoRatio = ((float) this.mVideoWidth) / ((float) this.mVideoHeight);
            if (sarNum > 0 && sarDen > 0) {
                videoRatio = (((float) sarNum) * videoRatio) / ((float) sarDen);
            }
            this.mSurfaceHeight = this.mVideoHeight;
            this.mSurfaceWidth = this.mVideoWidth;
            if (layout == 0 && this.mSurfaceWidth < windowWidth && this.mSurfaceHeight < windowHeight) {
                lp.width = (int) (((float) this.mSurfaceHeight) * videoRatio);
                lp.height = this.mSurfaceHeight;
            } else if (layout == 3) {
                lp.width = windowRatio > videoRatio ? windowWidth : (int) (((float) windowHeight) * videoRatio);
                lp.height = windowRatio < videoRatio ? windowHeight : (int) (((float) windowWidth) / videoRatio);
            } else {
                boolean full = layout == 2;
                int i = (full || windowRatio < videoRatio) ? windowWidth : (int) (((float) windowHeight) * videoRatio);
                lp.width = i;
                i = (full || windowRatio > videoRatio) ? windowHeight : (int) (((float) windowWidth) / videoRatio);
                lp.height = i;
            }
            setLayoutParams(lp);
            getHolder().setFixedSize(this.mSurfaceWidth, this.mSurfaceHeight);
            DebugLog.dfmt(TAG, "VIDEO: %dx%dx%f[SAR:%d:%d], Surface: %dx%d, LP: %dx%d, Window: %dx%dx%f", Integer.valueOf(this.mVideoWidth), Integer.valueOf(this.mVideoHeight), Float.valueOf(videoRatio), Integer.valueOf(this.mVideoSarNum), Integer.valueOf(this.mVideoSarDen), Integer.valueOf(this.mSurfaceWidth), Integer.valueOf(this.mSurfaceHeight), Integer.valueOf(lp.width), Integer.valueOf(lp.height), Integer.valueOf(windowWidth), Integer.valueOf(windowHeight), Float.valueOf(windowRatio));
        }
        this.mVideoLayout = layout;
    }

    private void initVideoView(Context ctx) {
        this.mContext = ctx;
        this.mVideoWidth = 0;
        this.mVideoHeight = 0;
        this.mVideoSarNum = 0;
        this.mVideoSarDen = 0;
        SurfaceHolder holder = getHolder();
        holder.addCallback(this.mSHCallback);
        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();
        this.mCurrentState = 0;
        this.mTargetState = 0;
        if (ctx instanceof Activity) {
            ((Activity) ctx).setVolumeControlStream(3);
        }
    }

    public boolean isValid() {
        return this.mSurfaceHolder != null && this.mSurfaceHolder.getSurface().isValid();
    }

    public void setVideoPath(String path) {
        setVideoURI(Uri.parse(path));
    }

    public void setVideoURI(Uri uri) {
        this.mUri = uri;
        this.mSeekWhenPrepared = 0;
        openVideo();
        requestLayout();
        invalidate();
    }

    public void setUriNull() {
        this.mUri = null;
    }

    public void setUserAgent(String ua) {
        this.mUserAgent = ua;
    }

    public void stopPlayback() {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.stop();
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
            this.mCurrentState = 0;
            this.mTargetState = 0;
        }
    }

    private void openVideo() {
        if (this.mUri != null && this.mSurfaceHolder != null) {
            Intent i = new Intent("com.android.music.musicservicecommand");
            i.putExtra("command", "pause");
            this.mContext.sendBroadcast(i);
            release(false);
            try {
                this.mDuration = -1;
                this.mCurrentBufferPercentage = 0;
                IjkMediaPlayer ijkMediaPlayer = null;
                if (this.mUri != null) {
                    ijkMediaPlayer = new IjkMediaPlayer();
                    ijkMediaPlayer.setOption(4, "overlay-format", 842225234);
                    ijkMediaPlayer.setOption(4, "framedrop", 12);
                    ijkMediaPlayer.setOption(1, "http-detect-range-support", 0);
                    ijkMediaPlayer.setOption(1, "user_agent", this.mUserAgent);
                    ijkMediaPlayer.setOption(2, "skip_loop_filter", 48);
                }
                this.mMediaPlayer = ijkMediaPlayer;
                this.mMediaPlayer.setOnPreparedListener(this.mPreparedListener);
                this.mMediaPlayer.setOnVideoSizeChangedListener(this.mSizeChangedListener);
                this.mMediaPlayer.setOnCompletionListener(this.mCompletionListener);
                this.mMediaPlayer.setOnErrorListener(this.mErrorListener);
                this.mMediaPlayer.setOnBufferingUpdateListener(this.mBufferingUpdateListener);
                this.mMediaPlayer.setOnInfoListener(this.mInfoListener);
                this.mMediaPlayer.setOnSeekCompleteListener(this.mSeekCompleteListener);
                if (this.mUri != null) {
                    this.mMediaPlayer.setDataSource(this.mUri.toString());
                }
                this.mMediaPlayer.setDisplay(this.mSurfaceHolder);
                this.mMediaPlayer.setScreenOnWhilePlaying(true);
                this.mMediaPlayer.prepareAsync();
                this.mCurrentState = 1;
                attachMediaController();
            } catch (IOException ex) {
                DebugLog.e(TAG, "Unable to open content: " + this.mUri, ex);
                this.mCurrentState = -1;
                this.mTargetState = -1;
                this.mErrorListener.onError(this.mMediaPlayer, 1, 0);
            } catch (IllegalArgumentException ex2) {
                DebugLog.e(TAG, "Unable to open content: " + this.mUri, ex2);
                this.mCurrentState = -1;
                this.mTargetState = -1;
                this.mErrorListener.onError(this.mMediaPlayer, 1, 0);
            }
        }
    }


    public void setMediaController(MediaController controller) {
        if (this.mMediaController != null) {
            this.mMediaController.hide();
        }
        this.mMediaController = controller;
        attachMediaController();
    }

    public void setMediaBufferingIndicator(View mediaBufferingIndicator) {
        if (this.mMediaBufferingIndicator != null) {
            this.mMediaBufferingIndicator.setVisibility(8);
        }
        this.mMediaBufferingIndicator = mediaBufferingIndicator;
    }

    private void attachMediaController() {
        if (this.mMediaPlayer != null && this.mMediaController != null) {
            this.mMediaController.setMediaPlayer(this);
            this.mMediaController.setAnchorView(getParent() instanceof View ? (View) getParent() : this);
            this.mMediaController.setEnabled(isInPlaybackState());
            if (this.mUri != null) {
                List<String> paths = this.mUri.getPathSegments();
                String name = (paths == null || paths.isEmpty()) ? "null" : (String) paths.get(paths.size() - 1);
                this.mMediaController.setFileName(name);
            }
        }
    }


    public void setOnPreparedListener(OnPreparedListener l) {
        this.mOnPreparedListener = l;
    }

    public void setOnCompletionListener(OnCompletionListener l) {
        this.mOnCompletionListener = l;
    }

    public void setOnErrorListener(OnErrorListener l) {
        this.mOnErrorListener = l;
    }

    public void setOnBufferingUpdateListener(OnBufferingUpdateListener l) {
        this.mOnBufferingUpdateListener = l;
    }

    public void setOnSeekCompleteListener(OnSeekCompleteListener l) {
        this.mOnSeekCompleteListener = l;
    }

    public void setOnInfoListener(OnInfoListener l) {
        this.mOnInfoListener = l;
    }

    private void release(boolean cleartargetstate) {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.reset();
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
            this.mCurrentState = 0;
            if (cleartargetstate) {
                this.mTargetState = 0;
            }
        }
    }

    public boolean onTouchEvent(MotionEvent ev) {
        if (isInPlaybackState() && this.mMediaController != null) {
            toggleMediaControlsVisiblity();
        }
        return false;
    }

    public boolean onTrackballEvent(MotionEvent ev) {
        if (isInPlaybackState() && this.mMediaController != null) {
            toggleMediaControlsVisiblity();
        }
        return false;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean isKeyCodeSupported = (keyCode == 4 || keyCode == 24 || keyCode == 25 || keyCode == 82 || keyCode == 5 || keyCode == 6) ? false : true;
        if (isInPlaybackState() && isKeyCodeSupported && this.mMediaController != null) {
            if (keyCode == 79 || keyCode == 85 || keyCode == 62) {
                if (this.mMediaPlayer.isPlaying()) {
                    pause();
                    this.mMediaController.show();
                    return true;
                }
                start();
                this.mMediaController.hide();
                return true;
            } else if (keyCode == 86 && this.mMediaPlayer.isPlaying()) {
                pause();
                this.mMediaController.show();
            } else {
                toggleMediaControlsVisiblity();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void toggleMediaControlsVisiblity() {
        if (this.mMediaController.isShowing()) {
            this.mMediaController.hide();
        } else {
            this.mMediaController.show();
        }
    }

    public void start() {
        if (isInPlaybackState()) {
            this.mMediaPlayer.start();
            this.mCurrentState = 3;
        }
        this.mTargetState = 3;
    }

    public void pause() {
        if (isInPlaybackState() && this.mMediaPlayer.isPlaying()) {
            this.mMediaPlayer.pause();
            this.mCurrentState = 4;
        }
        this.mTargetState = 4;
    }

    public void resume() {
        if (this.mSurfaceHolder == null && this.mCurrentState == 6) {
            this.mTargetState = 7;
        } else if (this.mCurrentState == 8) {
            openVideo();
        }
    }

    public int getDuration() {
        if (!isInPlaybackState()) {
            this.mDuration = -1;
            return (int) this.mDuration;
        } else if (this.mDuration > 0) {
            return (int) this.mDuration;
        } else {
            this.mDuration = this.mMediaPlayer.getDuration();
            return (int) this.mDuration;
        }
    }

    public int getCurrentPosition() {
        if (isInPlaybackState()) {
            return (int) this.mMediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    public void seekTo(long msec) {
        if (isInPlaybackState()) {
            this.mMediaPlayer.seekTo(msec);
            this.mSeekWhenPrepared = 0;
            return;
        }
        this.mSeekWhenPrepared = msec;
    }

    public boolean isPlaying() {
        return isInPlaybackState() && this.mMediaPlayer.isPlaying();
    }

    public int getBufferPercentage() {
        if (this.mMediaPlayer != null) {
            return this.mCurrentBufferPercentage;
        }
        return 0;
    }

    public int getVideoWidth() {
        return this.mVideoWidth;
    }

    public int getVideoHeight() {
        return this.mVideoHeight;
    }

    protected boolean isInPlaybackState() {
        return (this.mMediaPlayer == null || this.mCurrentState == -1 || this.mCurrentState == 0 || this.mCurrentState == 1) ? false : true;
    }

    public boolean canPause() {
        return this.mCanPause;
    }

    public boolean canSeekBackward() {
        return this.mCanSeekBack;
    }

    public boolean canSeekForward() {
        return this.mCanSeekForward;
    }
}