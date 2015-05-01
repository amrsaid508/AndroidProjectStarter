
package com.deardhruv.projectstarter.holders;

import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.view.View;
import jp.wasabeef.recyclerview.animators.holder.AnimateViewHolder;

class CardViewHolder extends AnimateViewHolder {

	public CardViewHolder(View itemView) {
		super(itemView);
	}

	@Override
	public void animateRemoveImpl(ViewPropertyAnimatorListener listener) {
		ViewCompat.animate(itemView).translationY(-itemView.getHeight() * 0.3f).alpha(0)
				.setDuration(300).setListener(listener).start();
	}

	@Override
	public void preAnimateAddImpl() {
		ViewCompat.setTranslationY(itemView, -itemView.getHeight() * 0.3f);
		ViewCompat.setAlpha(itemView, 0);
	}

	@Override
	public void animateAddImpl(ViewPropertyAnimatorListener listener) {
		ViewCompat.animate(itemView).translationY(0).alpha(1).setDuration(300).setListener(listener)
				.start();
	}
}
