package evgenykravtsov.appblocker.domain.model.exercise.pictures;

import java.util.List;
import java.util.Map;

public interface PicturesRepository {

    Map<PictureCategory, List<Picture>> getCategorizedPictures();
}
