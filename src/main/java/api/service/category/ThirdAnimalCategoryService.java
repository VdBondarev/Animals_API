package api.service.category;

import static api.constant.NumbersConstantsHolder.THIRD_CATEGORY_RANGE_FROM;
import static api.constant.NumbersConstantsHolder.THIRD_CATEGORY_RANGE_TO;
import static api.constant.NumbersConstantsHolder.THREE;

import org.springframework.stereotype.Service;

@Service
public class ThirdAnimalCategoryService implements AnimalCategoryService {

    @Override
    public Long getCategory(int cost) {
        return THREE;
    }

    @Override
    public boolean isApplicable(int cost) {
        return cost >= THIRD_CATEGORY_RANGE_FROM && cost <= THIRD_CATEGORY_RANGE_TO;
    }
}
