[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.thepavel.cubaentityloader/cubaentityloader-global/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.thepavel.cubaentityloader/cubaentityloader-global)

# Intro

This is an add-on for the CUBA Platform. It provides the `EntityLoader` helper bean. See below for the demos.

# Motivation

When building screen controllers or services I would like to have all queries organized in some way.

# Adding to your project

Compatible with CUBA 7.1.0 or newer.

Coordinates: `org.thepavel.cubaentityloader:cubaentityloader-global:1.0.1`

1. In the `build.gradle` file specify the coordinates in the dependencies section as follows:

```
dependencies {
    appComponent("com.haulmont.cuba:cuba-global:$cubaVersion")
    appComponent("org.thepavel.cubaentityloader:cubaentityloader-global:1.0.1")
}
```

2. Locate two `web.xml` files (one in the core and one in the web) and add `org.thepavel.cubaentityloader` to the appComponents parameter in each file:

```
<context-param>
    <param-name>appComponents</param-name>
    <param-value>com.haulmont.cuba org.thepavel.cubaentityloader</param-value>
</context-param>
```

3. Run CUBA / Re-import gradle project

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

- List&lt;T&gt; list();
- List&lt;T&gt; page(int firstResult, int maxResults);
- T one();
- Optional&lt;T&gt; optional();
- T one(K id);
- Optional&lt;T&gt; optional(K id);
- long count();
- QueryRunner&lt;T, K&gt; view(String view);
- QueryRunner&lt;T, K&gt; param(String name, Object value);
- QueryRunner&lt;T, K&gt; params(Map&lt;String, Object&gt; params);
