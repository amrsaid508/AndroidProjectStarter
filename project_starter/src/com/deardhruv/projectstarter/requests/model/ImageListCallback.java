
package com.deardhruv.projectstarter.requests.model;

import com.deardhruv.projectstarter.response.AbstractApiCallback;
import com.deardhruv.projectstarter.response.model.ImageListResponse;
import com.deardhruv.projectstarter.response.model.ImageResult;
import com.deardhruv.projectstarter.utils.Logger;

/**
 * This class should be used to have a callback which can be used for
 * "ImageList Requests". That means requests which have a
 * {@link ImageListResponse}.
 */
public class ImageListCallback extends AbstractApiCallback<ImageListResponse> {

	private static final String LOGTAG = ImageListCallback.class.getSimpleName();
	private static final Logger LOG = new Logger(LOGTAG);

	/**
	 * See super constructor
	 * {@link AbstractApiCallback#AbstractApiCallback(String)}.
	 */
	public ImageListCallback(String requestTag) {
		super(requestTag);
	}

	/**
	 * Saves ImageListResponse as file which can be used to read Category
	 * whenever needed.
	 */
	@Override
	protected void modifyResponseBeforeDelivery(ImageListResponse result) {
		int id = 41;
		String name = "lacinia nisi ";
		String img = "http://www.mediafire.com/convkey/c670/b4a7ppdlhghhai8zg.jpg";

		ImageResult imageResult = new ImageResult();
		imageResult.setId(id);
		imageResult.setName(name);
		imageResult.setImg(img);

		result.getData().getImageResultList().add(imageResult);
	}
}
