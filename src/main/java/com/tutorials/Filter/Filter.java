package com.tutorials.Filter;

import com.tutorials.entity.AnimalEntity;

/**
 * Created by vhomyak on 19.05.2017.
 */
public interface Filter {

    void execute(AnimalEntity animal);

    void setNextFilter( Filter nextfilter);


}
