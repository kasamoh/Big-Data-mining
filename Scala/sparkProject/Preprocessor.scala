package com.sparkProject
import org.apache.spark.sql.types.IntegerType
import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions.countDistinct
import org.apache.spark.sql._
import org.apache.spark.sql.functions._

object Preprocessor {

  def main(args: Array[String]): Unit = {

    // Des réglages optionels du job spark. Les réglages par défaut fonctionnent très bien pour ce TP
    // on vous donne un exemple de setting quand même
    val conf = new SparkConf().setAll(Map(
      "spark.scheduler.mode" -> "FIFO",
      "spark.speculation" -> "false",
      "spark.reducer.maxSizeInFlight" -> "48m",
      "spark.serializer" -> "org.apache.spark.serializer.KryoSerializer",
      "spark.kryoserializer.buffer.max" -> "1g",
      "spark.shuffle.file.buffer" -> "32k",
      "spark.default.parallelism" -> "12",
      "spark.sql.shuffle.partitions" -> "12"
    ))

    // Initialisation de la SparkSession qui est le point d'entrée vers Spark SQL (donne accès aux dataframes, aux RDD,
    // création de tables temporaires, etc et donc aux mécanismes de distribution des calculs.)
    val spark = SparkSession
      .builder
      .config(conf)
      .appName("TP_spark")
      .getOrCreate()

    import spark.implicits._ // << add this in rder to use


    // importing data
    val df = spark.read.format("csv").option("header", "true").load("/home/user/TelecomParistech/TP spark/tp2-problem/TP_ParisTech_2018_2019_starter/TP_ParisTech_2017_2018_starter/data/train_clean.csv")
    println("nombre__obs :"+df.count())

    println("nombre d'observation : "+df.count())

    println("nombre__colonnes : "+df.columns.size)


    println( df.printSchema())  //affichage des tpes des variables
    //df.show()

    // df.withColumn("col_name", df.col("col_name").cast(DataTypes.IntegerType))

    val dfCasted= df
      .withColumn("goal", df.col("goal").cast("Int"))
      .withColumn("deadline" , $"deadline".cast("Int"))
      .withColumn("state_changed_at", $"state_changed_at".cast("Int"))
      .withColumn("created_at", $"created_at".cast("Int"))
      .withColumn("launched_at", $"launched_at".cast("Int"))
      .withColumn("backers_count", $"backers_count".cast("Int"))
      .withColumn("final_status", $"final_status".cast("Int"))

   // dfCasted.printSchema()

    // df.withColumn("col_name", df.col("col_name").cast(DataTypes.IntegerType))
   // dfCasted.show()
    dfCasted.select("goal", "backers_count", "final_status").describe().show
    // dfCasted.groupBy("disable_communication").count.orderBy($"count".desc).show(100)


    dfCasted.groupBy("disable_communication").count.orderBy($"count".desc).show(100)
    dfCasted.groupBy("country").count.orderBy($"count".desc).show(100)
    dfCasted.groupBy("currency").count.orderBy($"count".desc).show(100)
    dfCasted.select("deadline").dropDuplicates.show()
    dfCasted.groupBy("state_changed_at").count.orderBy($"count".desc).show(100)
    dfCasted.groupBy("backers_count").count.orderBy($"count".desc).show(100)
    dfCasted.select("goal", "final_status").show(30)
    dfCasted.groupBy("country", "currency").count.orderBy($"count".desc).show(50)

    val df2: DataFrame = dfCasted.drop("disable_communication") // trop de false  ( true < 2% )

    val dfNoFutur: DataFrame = df2
      .drop("backers_count", "state_changed_at") //Ici, pour enlever les données du futur on retir les colonnes "backers_count" et "state_changed_at".




    def udfCountry = udf{(country: String, currency: String) =>
      if (country == "False")
        currency
      else
        country //: ((String, String) => String)  pour éventuellement spécifier le type
    }

    def udfCurrency = udf{(currency: String) =>
      if ( currency != null && currency.length != 3 )
        null
      else
        currency //: ((String, String) => String)  pour éventuellement spécifier le type
    }

    val dfCountry: DataFrame = dfNoFutur
      .withColumn("country2", udfCountry($"country", $"currency"))
      .withColumn("currency2", udfCurrency($"currency"))
      .drop("country", "currency")

    dfCountry.show()
    // Ou encore, en utilisant le "when" de sql.functions:
    //dfNoFutur
      //.withColumn("country2", when(condition=$"country"==="False", value=$"currency").otherwise($"country"))
      //.withColumn("currency2", when(condition=$"country".isNotNull && length($"currency")=!=3, value=null).otherwise($"currency"))
      //.drop("country", "currency")

    println("hello world ! from Preprocessor")


  }

}
