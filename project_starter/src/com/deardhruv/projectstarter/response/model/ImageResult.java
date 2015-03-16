
package com.deardhruv.projectstarter.response.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/** This class contains the detail information about Image List. */
public class ImageResult implements Serializable, Parcelable {

	private static final long serialVersionUID = 843633178166986606L;

	public ImageResult() {
		// nothing.
	}

	@JsonProperty("id")
	private int id;

	@JsonProperty("name")
	private String name;

	@JsonProperty("img")
	private String img;

	/** Parcelable implementation. */
	@Override
	public int describeContents() {
		return 0;
	}

	/** Parcelable implementation. */
	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(id);
		out.writeString(name);
		out.writeString(img);

	}

	/** Parcelable implementation. */
	public static final Parcelable.Creator<ImageResult> CREATOR = new Parcelable.Creator<ImageResult>() {
		public ImageResult createFromParcel(Parcel in) {
			return new ImageResult(in);
		}

		public ImageResult[] newArray(int size) {
			return new ImageResult[size];
		}
	};

	/** Parcelable implementation. */
	private ImageResult(Parcel in) {
		id = in.readInt();
		name = in.readString();
		img = in.readString();
	}

	public String getImg() {
		return img;
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

}
