## Modifications pour lancement :

Il y a des modifications à faire au niveau du fichier **build_and_submit.sh** :

Il faut changer path_to_spark si le dossier **spark-2.2.0-bin-hadoop2.7**  ne se trouve pas dans le "HOME"
```
path_to_spark="$HOME/spark-2.2.0-bin-hadoop2.7"
```

Modifier dans **Trainer.scala** le chemin vers le training set :

```
val input = ".../trainingset"
```


## Execution

```
./build_and_submit.sh Trainer
```

## Result : 

**f1score = 0,65**


| final_status | predictions | count |
|:---------:|:-----------:|:-------:|
| 1         | 0           |   1016  |
| 0         | 1           |   2865  |
| 1         | 1           |   2378  |
| 0         | 1           |   4391  |
