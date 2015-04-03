
package com.deardhruv.projectstarter.response.model;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

import com.deardhruv.projectstarter.response.AbstractApiResponse;
import com.deardhruv.projectstarter.utils.Dumper;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The root response object for the api call image_list_json. Contains a list of
 * images.
 */
public class UploadFileResponse extends AbstractApiResponse {

	private static final long serialVersionUID = 4136574699094835673L;

	@JsonProperty("results")
	private Results results;

	public UploadFileResponse() {
		// nothing.
	}

	class Results implements Serializable, Parcelable {

		private static final long serialVersionUID = 6148637452535436602L;

		public Results() {
		}

		@JsonProperty("name")
		private String name;

		@JsonProperty("url")
		private String url;

		@JsonProperty("size")
		private int size;

		/** Parcelable implementation. */
		@Override
		public int describeContents() {
			return 0;
		}

		/** Parcelable implementation. */
		@Override
		public void writeToParcel(Parcel out, int flags) {

			out.writeString(name);
			out.writeString(url);
			out.writeInt(size);

		}

		/** Parcelable implementation. */
		public final Parcelable.Creator<Results> CREATOR = new Parcelable.Creator<Results>() {
			public Results createFromParcel(Parcel in) {
				return new Results(in);
			}

			public Results[] newArray(int size) {
				return new Results[size];
			}
		};

		/** Parcelable implementation. */
		private Results(Parcel in) {

			name = in.readString();
			url = in.readString();
			size = in.readInt();

		}

		public String getName() {
			return name;
		}

		public String getUrl() {
			return url;
		}

		public int getSize() {
			return size;
		}

	}

	@Override
	public String toString() {
		return Dumper.dump(this);
	}

}
