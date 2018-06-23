/*******************************************************
 * Copyright (C) 2015 Mirco Timmermann - All Rights Reserved
 * 
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Mirco Timmermann <mtimmermann@gmx.de>, December 2015
 * 
 *******************************************************/
package com.yaselak.game.addtogdx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;


/*
 * Actor for sprite sheets.
 */
public class AnimatedActor extends Actor {
	protected Animation<TextureRegion> _animation = null;
	
	protected float _stateTime = 0;
	
	public boolean _flipX = false;
	public boolean _flipY = false;
	
	public boolean _autoSizeAdjust = true;
	public boolean _autoSizeAdjustFrame = false;
	public boolean _autoUpdateParentSize = false;
	
	
	public AnimatedActor() {
		
	}
	
	public AnimatedActor(TextureAtlas textureAtlas, String[] images, float speed, Animation.PlayMode playMode) {
		super();
		
		setAnimation(textureAtlas, images, speed, playMode);
	}

	public void setAnimation(Animation<TextureRegion> animation) {
		_animation = animation;
		
		if(_animation != null) {
			if(_autoSizeAdjust) { 
				this.setWidth(animation.getKeyFrame(0).getRegionWidth()); 
				this.setHeight(animation.getKeyFrame(0).getRegionHeight());
				
				if(_autoUpdateParentSize) {
					Actor parent = this.getParent();
					
					if(parent != null) {
						parent.setWidth(this.getWidth());
						parent.setHeight(this.getHeight());
					}
				}
			}
		}
	}
	
	public static Animation<TextureRegion> createAnimation(TextureAtlas textureAtlas, String[] images, float speed, Animation.PlayMode playMode) {
		Array<AtlasRegion> footage = new Array<AtlasRegion>();
		for(String image : images) {
			footage.add(textureAtlas.findRegion(image));
		}
		
		Animation<TextureRegion> animation = new Animation<TextureRegion>(speed, footage);
		animation.setPlayMode(playMode);
		
		return animation;
	}
	
	public Animation<TextureRegion> setAnimation(TextureAtlas textureAtlas, String[] images, float speed, Animation.PlayMode playMode) {
		return createAnimation(textureAtlas, images, speed, playMode);
	}
	
	@Override
	public void act(float delta) {
		_stateTime += delta;
		
		if(_autoSizeAdjustFrame) {
			TextureRegion textureRegion = _animation.getKeyFrame(_stateTime);
			
			this.setWidth(textureRegion.getRegionWidth()); 
			this.setHeight(textureRegion.getRegionHeight());
			
			if(_autoUpdateParentSize) {
				this.getParent().setWidth(this.getWidth());
				this.getParent().setHeight(this.getHeight());
			}
		}
		
		super.act(delta);
	}
	
	public void setFrameDuration(float frameDuration) {
		if(_animation != null) {
			_animation.setFrameDuration(frameDuration);
		}
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		if(_animation != null) {
			float x = getX();
			if(_flipX) x += getWidth();
			
			float y = getY();
			if(_flipY) y += getHeight();
			
			Color color = batch.getColor();
			batch.setColor(this.getColor());
			
			batch.draw(_animation.getKeyFrame(_stateTime), x, y, getOriginX(), getOriginY(), getWidth(), getHeight(), (_flipX ? -this.getScaleX() : this.getScaleY()), (_flipY ? -this.getScaleY() : this.getScaleY()), getRotation());
		
			batch.setColor(color);
		}
	}
	
	public void resetStateTime() {
		_stateTime = 0;
	}
	
	public void SetStateTime(float time) {
		_stateTime = time;
	}
	
	public float GetStateTime() {
		return _stateTime;
	}
	
	public boolean isAnimationFinished() {
		return _animation.isAnimationFinished(_stateTime);
	}
	
	public int GetKeyFrameIndex() {
		return _animation.getKeyFrameIndex(_stateTime);
	}
	
	public TextureRegion GetKeyFrame() {
		return _animation.getKeyFrame(_stateTime);
	}
	
	public float GetFrameDuration() {
		if(_animation != null) {
			return _animation.getFrameDuration();
		}
		
		return 0;
	}
	
	public int GetFrameLength() {
		if(_animation != null) {
			return _animation.getKeyFrames().length;
		}
		
		return 0;
	}
	
	public Animation<TextureRegion> getAnimation() {
		return _animation;
	}
	
	public boolean hasAnimation() {
		return _animation != null;
	}
	
	public float getValue() {
		int length = GetFrameLength();
		float frameNumber = getFrameNumber();
		
		float value = (1.0f/length)*frameNumber;
		
		//
		return value;
	}
	
	public int getValue_int() {
		return (int)getValue();
	}
	
	public float getNormedValueFrame() {
		float frameNumber = getFrameNumber();
		int frameNumber_int = (int)frameNumber;
		
		float value = (frameNumber-frameNumber_int);
		
		//
		return value;
	}
	
	public float getNormedValue() {
		float value = getValue();
		int value_int = (int) value;
		
		//
		return (value - value_int);
	}
	
	public float GetNormedValueHalf() {
		float value = getNormedValue();
		
		if(value < 0.5f) {
			value = value*2;
		} else {
			value = 2.0f-(value*2);
		}
		
		//
		return value;
	}
	
	public float getFrameNumber() {
		float stateTime = GetStateTime();
		float frameDuration = GetFrameDuration();
		
		float frameNumber = (stateTime / frameDuration);
		return frameNumber;
	}
	
	public boolean isAnimationPlaying(Animation<TextureRegion> anim) {
		Animation<TextureRegion> animation = this.getAnimation();
		if(animation == null) return false;
		
		if(animation == anim) {
			return true;
		}

		return false;
	}
}
