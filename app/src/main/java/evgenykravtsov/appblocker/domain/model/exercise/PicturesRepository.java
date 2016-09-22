package evgenykravtsov.appblocker.domain.model.exercise;

import java.util.List;
import java.util.Map;

import evgenykravtsov.appblocker.domain.model.exercise.pictures.PictureCategory;

public interface PicturesRepository {

    Map<PictureCategory, List<Picture>> getCategorizedPictures();

    List<Picture> getMemoryPictures(int numberOfPictures);
}
