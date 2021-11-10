package org.metadatacenter.fairware.core.services.bioportal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BpPagedResults<T>
{

  private Integer page;
  private Integer pageCount;
  private Integer totalCount;
  private Integer prevPage;
  private Integer nextPage;
  private List<T> collection;

  public BpPagedResults() {}

  public BpPagedResults(Integer page, Integer pageCount, Integer totalCount, Integer prevPage, Integer nextPage, List<T> collection)
  {
    this.page = page;
    this.pageCount = pageCount;
    this.totalCount = totalCount;
    this.prevPage = prevPage;
    this.nextPage = nextPage;
    this.collection = collection;
  }

  public Integer getPage()
  {
    return page;
  }

  public Integer getPageCount()
  {
    return pageCount;
  }

  public Integer getTotalCount() { return totalCount; }

  public Integer getPrevPage()
  {
    return prevPage;
  }

  public Integer getNextPage()
  {
    return nextPage;
  }

  public List<T> getCollection()
  {
    return collection;
  }

  public void setCollection(List<T> collection)
  {
    this.collection = collection;
  }
}
