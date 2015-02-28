
package com.deardhruv.projectstarter.response.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;

/** This class contains the detail information about Image List. */
public class ImageListResult implements Parcelable {

	public ImageListResult() {
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
	public static final Parcelable.Creator<ImageListResult> CREATOR = new Parcelable.Creator<ImageListResult>() {
		public ImageListResult createFromParcel(Parcel in) {
			return new ImageListResult(in);
		}

		public ImageListResult[] newArray(int size) {
			return new ImageListResult[size];
		}
	};

	/** Parcelable implementation. */
	private ImageListResult(Parcel in) {
		id = in.readInt();
		name = in.readString();
		img = in.readString();
	}

}
