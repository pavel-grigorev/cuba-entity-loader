# Intro

This is an add-on for the CUBA Platform. It provides the `EntityLoader` helper bean. See below for the demos.

# Motivation

When building screen controllers or services I would like to have all queries organized in some way. 

# Adding to your project

Coordinates: `org.thepavel.cubaentityloader:cubaentityloader-global:1.0.0`

See [Installing Add-on by Coordinates](https://doc.cuba-platform.com/studio/#addons_installing_by_coordinates)

Compatible with CUBA 7.1.0 or newer.

# Basic example

Let's say there's a screen controller class with the following code:

```java
@Inject
private DataManager dataManager;

private List<JobPosition> getJobPositions() {
  return dataManager
      .load(JobPosition.class)
      .view(View.LOCAL)
      .query("select e from cubatest_JobPosition e")
      .list();
}
```

An alternative approach using the `EntityLoader` would be:

```java
private EntityLoader<JobPosition, UUID> jobPositionLoader;

private List<JobPosition> getJobPositions() {
  return jobPositionLoader.list();
}
```

By simply adding an instance field of type `EntityLoader` to your screen controller class or to a spring bean class you are ready to run the basic "select all" query. The default view is "_local".

# Customizing queries

This is how you specify a custom query for the `EntityLoader`:

```java
@Query("select e from cubatest_JobPosition e where e.status <> 'deleted'")
private EntityLoader<JobPosition, UUID> jobPositionLoader;

private List<JobPosition> getJobPositions() {
  return jobPositionLoader.list();
}
```

# Parameterizing queries

```java
@Query("select e from cubatest_JobPosition e where e.status <> :status")
private EntityLoader<JobPosition, UUID> jobPositionLoader;

private List<JobPosition> getJobPositions() {
  return jobPositionLoader.param("status", "deleted").list();
}
```

# Binding query parameters to methods

```java
@Query("select e from cubatest_Document e where lower(e.name) like concat('%', lower(:name), '%')")
@QueryParam(name = "name", supplier = "getNameFilter")
private EntityLoader<Document, UUID> documentLoader;

private String getNameFilter() {
  return nameFilterField.getValue();
}

private List<Document> getDocuments() {
  return documentLoader.list();
}
```

# Binding query parameters to methods #2

```java
@Query("select e from cubatest_Document e where lower(e.name) like concat('%', lower(:name), '%')")
@QueryParams("getParams")
private EntityLoader<Document, UUID> documentLoader;

private Map<String, Object> getParams() {
  Map<String, Object> params = new HashMap<>();
  params.put("name", nameFilterField.getValue());
  return params;
}

private List<Document> getDocuments() {
  return documentLoader.list();
}
```

# Specifying a view

The default view is "_local". To change the view add an annotation:

```java
@Query("select e from cubatest_Document e where lower(e.name) like concat('%', lower(:name), '%')")
@QueryParam(name = "name", supplier = "getNameFilter")
@QueryView("document-search")
private EntityLoader<Document, UUID> documentLoader;

private String getNameFilter() {
  return nameFilterField.getValue();
}

private List<Document> getDocuments() {
  return documentLoader.list();
}
```

# Same query / different views

```java
@Service(DocumentService.NAME)
public class DocumentServiceBean implements DocumentService {
  @Inject
  private UserSessionSource userSessionSource;

  @Query("select e from cubatest_Document e " +
      "where lower(e.name) like concat('%', lower(:name), '%') " +
      "and e.createdBy = :createdBy")
  @QueryParam(name = "createdBy", supplier = "currentUser")
  @QueryView("document-browse")
  private EntityLoader<MyEntity, UUID> entityLoader;

  private String currentUser() {
    return userSessionSource.getUserSession().getUser().getLogin();
  }

  @Override
  public List<MyEntity> getDocuments(String nameFilter) {
    return entityLoader.param("name", nameFilter).list();
  }

  @Override
  public List<MyEntity> getDocuments(String nameFilter, String view) {
    // Optionally you can override the view specified in the annotation
    return entityLoader.param("name", nameFilter).view(view).list();
  }
}
```

# Secured data manager

In order to run a query using a secured data manager add an annotation:

```java
@Query("select e from cubatest_JobPosition e where e.status <> :status")
@Secure
private EntityLoader<JobPosition, UUID> jobPositionLoader;

private List<JobPosition> getJobPositions() {
  return jobPositionLoader.param("status", "deleted").list();
}
```

# Complete list of methods

- List<T> list();
- List<T> page(int firstResult, int maxResults);
- T one();
- Optional<T> optional();
- T one(K id);
- Optional<T> optional(K id);
- long count();
- QueryRunner<T, K> view(String view);
- QueryRunner<T, K> param(String name, Object value);
- QueryRunner<T, K> params(Map<String, Object> params);