package com.sillycat.easycassandraserver.apps

import me.prettyprint.hector.api.{Keyspace, Cluster}
import me.prettyprint.hector.api.factory.HFactory
import me.prettyprint.cassandra.model.BasicColumnFamilyDefinition
import me.prettyprint.hector.api.ddl.{KeyspaceDefinition, ColumnFamilyDefinition, ComparatorType}
import me.prettyprint.cassandra.service.ThriftCfDef
import java.util.Arrays
import me.prettyprint.hector.api.mutation.Mutator
import me.prettyprint.cassandra.serializers.{LongSerializer, StringSerializer, BigDecimalSerializer}
import org.joda.time.DateTime
import me.prettyprint.hector.api.query.{QueryResult, ColumnQuery}
import me.prettyprint.hector.api.beans.HColumn
import com.sillycat.easycassandraserver.models.Product
import scala.Product


/**
 * Created with IntelliJ IDEA.
 * User: carl
 * Date: 6/20/13
 * Time: 5:22 PM
 * To change this template use File | Settings | File Templates.
 */



object CassandraShemaDataDemoApp extends App{
  val clusterName = "TestCluster"
  val host = "localhost"
  val port = 9160

  val keyspaceName = "sillycat"
  val columnFamilyName = "products"

  val cluster: Cluster = HFactory.getOrCreateCluster("TestCluster", host + ":" + 9160)

  //drop the schema
  cluster.describeKeyspace(keyspaceName) match {
    case null => println("There is no keyspace yet.")
    case _    => cluster.dropKeyspace(keyspaceName)
  }

  //define the schema
  val columnFamilyProducts: BasicColumnFamilyDefinition = new BasicColumnFamilyDefinition()
  columnFamilyProducts.setKeyspaceName(keyspaceName)
  columnFamilyProducts.setName(columnFamilyName)

  columnFamilyProducts.setKeyValidationClass(ComparatorType.UTF8TYPE.getClassName())
  columnFamilyProducts.setComparatorType(ComparatorType.UTF8TYPE)

  //columnFamilyProducts.setDefaultValidationClass(ComparatorType.UTF8TYPE.getClassName())
  //columnFamilyProducts.setDefaultValidationClass(ComparatorType.DECIMALTYPE.getClassName())
  //columnFamilyProducts.setDefaultValidationClass(ComparatorType.ASCIITYPE.getClassName())
  //columnFamilyProducts.setDefaultValidationClass(ComparatorType.COMPOSITETYPE.getClassName())


  val productsFamily: ColumnFamilyDefinition = new ThriftCfDef(columnFamilyProducts);

  val keyspaceDefinition: KeyspaceDefinition =
    HFactory.createKeyspaceDefinition(keyspaceName, "org.apache.cassandra.locator.SimpleStrategy",
      1, Arrays.asList(productsFamily))

  cluster.addKeyspace(keyspaceDefinition);

  //insert data
  val keyspaceOperator: Keyspace = HFactory.createKeyspace(keyspaceName, cluster)

  val mutator: Mutator[String]  = HFactory.createMutator(keyspaceOperator, StringSerializer.get())

  val p1 = Product(Some(1), "IPHONE5", DateTime.now, 499.99, "iphone5", "China")
  val p2 = Product(Some(2), "IPHONE4", DateTime.now, 199.99, "iphone4", "US")
  val p3 = Product(Some(3), "IPHONE4S", DateTime.now, 299.99, "iphone4s", "TW")

  //single insert p1
  mutator.insert(
    p1.productCode,
    columnFamilyName,
    HFactory.createColumn("ProductName",
      p1.productName,
      StringSerializer.get(),
      StringSerializer.get()
    )
  )

  mutator.insert(
    p1.productCode,
    columnFamilyName,
    HFactory.createColumn("Country",
      p1.country,
      StringSerializer.get(),
      StringSerializer.get()
    )
  )

  mutator.insert(
    p1.productCode,
    columnFamilyName,
    HFactory.createColumn("ProductPrice",
      java.math.BigDecimal.valueOf(p1.productPrice.doubleValue()),
      StringSerializer.get(),
      BigDecimalSerializer.get()
    )
  )

  //get p1
  val bigdecimalColumnQuery: ColumnQuery[String, String, java.math.BigDecimal] = HFactory.createColumnQuery(keyspaceOperator,
    StringSerializer.get(),
    StringSerializer.get(),
    BigDecimalSerializer.get())
  bigdecimalColumnQuery.setColumnFamily(columnFamilyName).setKey(p1.productCode).setName("ProductPrice")
  val result1: QueryResult[HColumn[String, java.math.BigDecimal]]  = bigdecimalColumnQuery.execute()

  val stringColumnQuery: ColumnQuery[String, String, String] = HFactory.createColumnQuery(keyspaceOperator,
    StringSerializer.get(),
    StringSerializer.get(),
    StringSerializer.get())
  stringColumnQuery.setColumnFamily(columnFamilyName).setKey(p1.productCode).setName("ProductName")
  val result2: QueryResult[HColumn[String, String]]  = stringColumnQuery.execute()

  stringColumnQuery.setColumnFamily(columnFamilyName).setKey(p1.productCode).setName("Country")
  val result3: QueryResult[HColumn[String, String]]  = stringColumnQuery.execute()

  println("Read HColumn from cassandra: " + result1.get())
  println("Read HColumn from cassandra: " + result2.get())
  println("Read HColumn from cassandra: " + result3.get())
  println("Verify on CLI with:  get products['iphone5']; ")

  //multiple insert p2,p3
  //p2
  mutator.addInsertion(
    p2.productCode,
    columnFamilyName,
    HFactory.createColumn("ProductName",
      p2.productName,
      StringSerializer.get(),
      StringSerializer.get()
    )
  )

  mutator.addInsertion(
    p2.productCode,
    columnFamilyName,
    HFactory.createColumn("Country",
      p2.country,
      StringSerializer.get(),
      StringSerializer.get()
    )
  )

  mutator.addInsertion(
    p2.productCode,
    columnFamilyName,
    HFactory.createColumn("ProductPrice",
      java.math.BigDecimal.valueOf(p2.productPrice.doubleValue()),
      StringSerializer.get(),
      BigDecimalSerializer.get()
    )
  )

  //p3
  mutator.addInsertion(
    p3.productCode,
    columnFamilyName,
    HFactory.createColumn("ProductName",
      p3.productName,
      StringSerializer.get(),
      StringSerializer.get()
    )
  )

  mutator.addInsertion(
    p3.productCode,
    columnFamilyName,
    HFactory.createColumn("Country",
      p3.country,
      StringSerializer.get(),
      StringSerializer.get()
    )
  )

  mutator.addInsertion(
    p3.productCode,
    columnFamilyName,
    HFactory.createColumn("ProductPrice",
      java.math.BigDecimal.valueOf(p3.productPrice.doubleValue()),
      StringSerializer.get(),
      BigDecimalSerializer.get()
    )
  )

  mutator.execute()

  bigdecimalColumnQuery.setColumnFamily(columnFamilyName).setKey(p2.productCode).setName("ProductPrice")
  val result4: QueryResult[HColumn[String, java.math.BigDecimal]]  = bigdecimalColumnQuery.execute()

  println("Read HColumn from cassandra: " + result4.get())
  println("Verify on CLI with:  get products['iphone4s']; ")

  cluster.getConnectionManager.shutdown()
}
