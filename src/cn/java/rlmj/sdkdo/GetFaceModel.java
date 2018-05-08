package cn.java.rlmj.sdkdo;

import cn.java.rlmj.service.PeopleService;

public class GetFaceModel {

	private PeopleService peopleService;

	private static GetFaceModel getFaceModel;

	private GetFaceModel(PeopleService peopleService) {
		this.peopleService = peopleService;
	}

	public void selectFaceModels() {
//		System.out.println(peopleService);
		FaceModels.facemodel = peopleService.getModels();
//		System.out.println("facemodels's length=" + FaceModels.facemodel.size());
//		System.out.println("test2" + FaceModels.facemodel.get(0).getHouse_id());
		// FaceModels.facemodel.add(face);
	}

	public static void getFaceModels(PeopleService peopleService) {
		if (getFaceModel == null)
			getFaceModel = new GetFaceModel(peopleService);
		getFaceModel.selectFaceModels();
	}
}
