
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

	class Results {
		@JsonProperty("file")
		private FileKey file;

		public FileKey getFile() {
			return file;
		}

	}

	class FileKey implements Serializable, Parcelable {
		
		private static final long serialVersionUID = 6148637452535436602L;

		public FileKey() {
		}

		@JsonProperty("name")
		private String name;

		@JsonProperty("url")
		private String url;

		@JsonProperty("type")
		private String type;

		@JsonProperty("size")
		private int size;

		@JsonProperty("delete_key")
		private String delete_key;

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
			out.writeString(type);
			out.writeInt(size);
			out.writeString(delete_key);

		}

		/** Parcelable implementation. */
		public final Parcelable.Creator<FileKey> CREATOR = new Parcelable.Creator<FileKey>() {
			public FileKey createFromParcel(Parcel in) {
				return new FileKey(in);
			}

			public FileKey[] newArray(int size) {
				return new FileKey[size];
			}
		};

		/** Parcelable implementation. */
		private FileKey(Parcel in) {

			name = in.readString();
			url = in.readString();
			type = in.readString();
			size = in.readInt();
			delete_key = in.readString();

		}

		public String getName() {
			return name;
		}

		public String getUrl() {
			return url;
		}

		public String getType() {
			return type;
		}

		public int getSize() {
			return size;
		}

		public String getDelete_key() {
			return delete_key;
		}

	}

	@Override
	public String toString() {
		return Dumper.dump(this);
	}

}
