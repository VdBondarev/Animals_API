package api.service.category;

import static api.constant.NumbersConstantsHolder.FOUR;
import static api.constant.NumbersConstantsHolder.FOURTH_CATEGORY_RANGE;

import org.springframework.stereotype.Service;

@Service
public class FourthAnimalCategoryService implements AnimalCategoryService {

    @Override
    public Long getCategory(int cost) {
        return FOUR;
    }

    @Override
    public boolean isApplicable(int cost) {
        return cost >= FOURTH_CATEGORY_RANGE;
    }
}
