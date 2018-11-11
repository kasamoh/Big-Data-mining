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


### Afin d'exécuter le programme Trainer.scala depuis IntelliJ , il faut aller à l'onglet "terminal" et tapper : 

```
./build_and_submit.sh Trainer
```

### Le programme devrait prendre entre 5 et 7 minutes pour s'éxécuter . A la fin , on obtient le score ci-dessous : 

**F1 score = 0.655**


| final_status | predictions | count |
|:---------:|:-----------:|:-------:|
| 1         | 0           |   1020  |
| 0         | 1           |   2836  |
| 1         | 1           |   2446  |
| 0         | 1           |   4512  |
