package evgenykravtsov.appblocker.external.android;

import android.content.Context;
import android.content.res.Resources;

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

        List<Picture> catsCategoryPictures = new ArrayList<>();
        List<Picture> carsCategoryPictures = new ArrayList<>();
        List<Picture> dogsCategoryPictures = new ArrayList<>();
        List<Picture> flowersCategoryPictures = new ArrayList<>();

        for (int i = 1; i < 13; i++) {
            Picture catsCategoryPicture = new Picture(
                    context.getResources().getDrawable(
                            getResourceIdByString("picture_exercise_cats_" + i))
            );

            catsCategoryPictures.add(catsCategoryPicture);
        }

        for (int i = 1; i < 10; i++) {
            Picture carsCategoryPicture = new Picture(
                    context.getResources().getDrawable(
                            getResourceIdByString("picture_exercise_cars_" + i))
            );

            carsCategoryPictures.add(carsCategoryPicture);
        }

        for (int i = 1; i < 14; i++) {
            Picture dogsCategoryPicture = new Picture(
                    context.getResources().getDrawable(
                            getResourceIdByString("picture_exercise_dogs_" + i))
            );

            dogsCategoryPictures.add(dogsCategoryPicture);
        }

        for (int i = 1; i < 17; i++) {
            Picture flowersCategoryPicture = new Picture(
                    context.getResources().getDrawable(
                            getResourceIdByString("picture_exercise_flowers_" + i))
            );

            flowersCategoryPictures.add(flowersCategoryPicture);
        }

        categorizedPictures.put(PictureCategory.Cats, catsCategoryPictures);
        categorizedPictures.put(PictureCategory.Cars, carsCategoryPictures);
        categorizedPictures.put(PictureCategory.Dogs, dogsCategoryPictures);
        categorizedPictures.put(PictureCategory.Flowers, flowersCategoryPictures);

        return categorizedPictures;
    }

    private int getResourceIdByString(String idTitle) {
        Resources resources = context.getResources();
        return resources.getIdentifier(idTitle, "drawable", context.getPackageName());
    }
}
