package solo.customloadingfragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

public class ListFragment extends android.support.v4.app.ListFragment {

	private ViewGroup mProgressContainer;
	private ImageView mEmptyImageView;
	private TextView mEmptyTextView;
	private View mEmptyView;
	private View mListContainer;
	private boolean mListShown;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list_content, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		mProgressContainer = (ViewGroup) view.findViewById(R.id.progressContainer);
		mListContainer = view.findViewById(R.id.listContainer);
		mEmptyView = view.findViewById(android.R.id.empty);
		mEmptyImageView = (ImageView) view.findViewById(R.id.empty_image);
		mEmptyTextView = (TextView) view.findViewById(R.id.empty_text);
		
		mListShown = true;
	}
	
	@Override
	public void setListAdapter(ListAdapter adapter) {
		super.setListAdapter(adapter);
		boolean hadAdapter = getListAdapter() != null;
		if (getListView() != null) {
			getListView().setAdapter(adapter);
            if (!mListShown && !hadAdapter) {
                // The list was hidden, and previously didn't have an
                // adapter.  It is now time to show it.
                setListShown(true, getView().getWindowToken() != null);
            }
        }
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		mListShown = false;
	}
	
	public void setEmptyImage(int resId) {
		if (mEmptyImageView != null) {
			mEmptyImageView.setImageResource(resId);
		}
	}
	
	public void setEmptyImage(Drawable drawable) {
		if (mEmptyImageView != null) {
			mEmptyImageView.setImageDrawable(drawable);
		}
	}
	
	@Override
	public void setEmptyText(CharSequence text) {
		if (mEmptyTextView != null) {
			mEmptyTextView.setText(text);
		}
	}
	
	public void setEmptyClickListener(OnClickListener listener) {
		if (mEmptyView != null) {
			mEmptyView.setOnClickListener(listener);
		}
	}
	
	public void setLoadingView(View view) {
		if (mProgressContainer != null) {
			if (mProgressContainer.getChildCount() > 0) {
				mProgressContainer.removeAllViews();
			}
			mProgressContainer.addView(view);
		}
	}
	
	@Override
	public void setListShown(boolean shown) {
		setListShown(shown, true);
	}
	
	@Override
	public void setListShownNoAnimation(boolean shown) {
		 setListShown(shown, false);
	}
	
	private void setListShown(boolean shown, boolean animate) {
		if (mListShown == shown) {
            return;
        }
        mListShown = shown;
		if (shown) {
            if (animate) {
                mProgressContainer.startAnimation(AnimationUtils.loadAnimation(
                        getActivity(), android.R.anim.fade_out));
                mListContainer.startAnimation(AnimationUtils.loadAnimation(
                        getActivity(), android.R.anim.fade_in));
            } else {
                mProgressContainer.clearAnimation();
                mListContainer.clearAnimation();
            }
            mProgressContainer.setVisibility(View.GONE);
            mListContainer.setVisibility(View.VISIBLE);
        } else {
            if (animate) {
                mProgressContainer.startAnimation(AnimationUtils.loadAnimation(
                        getActivity(), android.R.anim.fade_in));
                mListContainer.startAnimation(AnimationUtils.loadAnimation(
                        getActivity(), android.R.anim.fade_out));
            } else {
                mProgressContainer.clearAnimation();
                mListContainer.clearAnimation();
            }
            mProgressContainer.setVisibility(View.VISIBLE);
            mListContainer.setVisibility(View.GONE);
        }
	}
}
