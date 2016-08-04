package evgenykravtsov.appblocker.external.android;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import evgenykravtsov.appblocker.R;
import evgenykravtsov.appblocker.domain.model.exercise.pictures.Picture;
import evgenykravtsov.appblocker.domain.model.exercise.pictures.PictureCategory;
import evgenykravtsov.appblocker.domain.model.exercise.pictures.PicturesRepository;

public class InternalPicturesRepository implements PicturesRepository {

    private Context context;

    ////

    public InternalPicturesRepository(Context context) {
        this.context = context;
    }

    ////

    @Override
    public Map<PictureCategory, List<Picture>> getCategorizedPictures() {
        Map<PictureCategory, List<Picture>> categorizedPictures = new HashMap<>();

        List<Picture> transportCategoryPictures = new ArrayList<>();
        List<Picture> toolsCategoryPictures = new ArrayList<>();
        List<Picture> clothesCategoryPictures = new ArrayList<>();
        List<Picture> vegetablesCategoryPictures = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            Picture transportCategoryPicture = new Picture(
                    context.getResources().getDrawable(R.drawable.rectangle_blue)
            );
            Picture toolsCategoryPicture = new Picture(
                    context.getResources().getDrawable(R.drawable.rectangle_green)
            );
            Picture clothesCategoryPicture = new Picture(
                    context.getResources().getDrawable(R.drawable.rectangle_orange)
            );
            Picture vegetablesCategoryPicture = new Picture(
                    context.getResources().getDrawable(R.drawable.rectangle_red)
            );

            transportCategoryPictures.add(transportCategoryPicture);
            toolsCategoryPictures.add(toolsCategoryPicture);
            clothesCategoryPictures.add(clothesCategoryPicture);
            vegetablesCategoryPictures.add(vegetablesCategoryPicture);
        }

        categorizedPictures.put(PictureCategory.Transport, transportCategoryPictures);
        categorizedPictures.put(PictureCategory.Tools, toolsCategoryPictures);
        categorizedPictures.put(PictureCategory.Clothes, clothesCategoryPictures);
        categorizedPictures.put(PictureCategory.Vegetables, vegetablesCategoryPictures);

        return categorizedPictures;
    }
}
