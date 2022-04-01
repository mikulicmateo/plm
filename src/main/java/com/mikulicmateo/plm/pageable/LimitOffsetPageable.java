package com.mikulicmateo.plm.pageable;

import com.mikulicmateo.plm.exception.ResponseException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class LimitOffsetPageable implements Pageable {

    private final int limit;
    private final int offset;

    public LimitOffsetPageable(int limit, int offset){
        if (offset < 0) {
            throw new ResponseException("Offset index must not be less than zero!");
        }
        if (limit < 1) {
            throw new ResponseException("Limit must not be less than one!");
        }
        this.limit = limit;
        this.offset = offset;
    }

    @Override
    public int getPageNumber() {
        return 0;
    }

    @Override
    public int getPageSize() {
        return this.limit;
    }

    @Override
    public long getOffset() {
        return this.offset;
    }

    @Override
    public Sort getSort() {
        return Sort.unsorted();
    }

    @Override
    public Pageable next() {
        return null;
    }

    @Override
    public Pageable previousOrFirst() {
        return this;
    }

    @Override
    public Pageable first() {
        return this;
    }

    @Override
    public Pageable withPage(int pageNumber) {
        return null;
    }

    @Override
    public boolean hasPrevious() {
        return false;
    }
}
