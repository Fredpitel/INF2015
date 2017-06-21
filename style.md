Équipe 4 : Style du code Java
========================
----------

##Description :
Puisque Java possède déjà une convention produite par Sun Microsystem, nous nous baserons sur celle-ci. Ce fichier ne traitera donc que des éléments les plus pertinents ou que nous avons changé. Pour toutes caractéristiques de style non définie ici, veuillez vous référer au document  « [Java Code Conventions][1] ». Il est suggéré de prendre connaissance de la convention de Sun Microsystem avant de poursuivre ici.

###Organisation des fichiers :

 1. Déclaration du packetage et des imports
 2. Déclaration de la classe ou de l'interface
 3. Variables static de classe (publique, protégée puis privée)
 4. Variables d'instance (publique, protégée puis privée)
 5. Constructeurs
 6. Méthodes
 
####Structure des méthodes :
* Les méthodes ne devraient pas dépasser 10 lignes de code
* Chaque méthode doit respecter le principe de responsabilité unique

###Déclaration :
* Une seule déclaration par ligne
* Faire les déclarations au début d'un bloc

	     void MaMethode() {
		     int val1;    // début du bloc de la méthode
		     
		     if (condition) {
		         int val2;  // début du bloc if
		         ...
		     }
		 }

* La seule exception est pour un index d'une boucle for :

	    for (int i = 0; i < max;  i++) { ...

###Utilisation de if-else-if :
L'utilisation de if-else devrait se faire de cette forme:

    if (condition) {
        ...
    }

	if (condition) {
	    ...
	} else {
	    ...
	}

	if (condition) {
	    ...
	} else if (condition) {
	    ...
	} else if (condition) {
	    ...
	}

**Note**: Toujours utiliser les parenthèses. Incluant les cas où l'action ne tient que sur une ligne

###Utilisation de la boucle for :
La boucle for devrait avoir cette forme:

    for (initialisation; condition; update {
        ...
    }

###Utilisation de la boucle while :
La boucle while devrait avoir cette forme:

    while (condition) {
        ...
    }

### Utilisation de la boucle do-while :
La boucle do-while devrait avoir cette forme:

    do {
        ...
    while (condition);

###Utilisation du switch :
L'utilisation d'un switch devrait prendre cette forme:

    switch (condition) {
    case abc:
        ...
	    /* execute aussi le suivant */
	case def:
	    ...
	    break;

	case xyz:
	    ...
	    break;

	default:
	    ...
	    break;
	}

**Note**: Dans le cas où un case éxécute aussi de case suivant (il n'y a pas de break), ajouter un commentaire à l'endroit où le break aurait normalement dû se trouver.

###Utilisation de try-catch :
Un bloc try-catch devrait avoir cette forme:

    try {
        ...
    } catch (ExceptionClass e) {
        ...
    }

###Espacement :

####Indentation :
* Indentation à 4 espaces
* 80 caractères par lignes

####Utiliser deux saut de lignes dans les situations suivantes :

 * Entre les sections d'un fichier source
 * Entre les définitions des classes et des interfaces

####Utiliser un saut de ligne dans les situations suivantes :

* Entre les méthodes
* Entre les variables locales d'une méthode et ses premières instructions
* Devant un block ou une seule ligne de commentaire
* Entre les sections logiques d'une méthode afin d'accroitre sa lisibilité

####Utiliser un espace dans les situations suivantes :

* Un mot clé suivit d'une parenthèse devraient être déparé par un espace. Exemple:

	    while (true) {
	        ...
		}

* Un espace devrait être présent à la suite d'une virgule dans une liste d'arguments
* Tous les opérateurs binaires sauf  **.** devraient être séparé de leur opérande par un espace

	    a += c + d;
	    a = (a + b) / (c * d);

* Les expressions dans une instruction for devraient être séparées par un espace

	    for (expr1; expr2; expr3)
	    
* Un cast doit être suivi d'un espace

	    maMethode((byte) aNum, (Object) x);


###Nomage :

####Langage :
Le code doit être écrit en français. Il y a cependant des exceptions pour le noms des méthodes pour les termes anglais suivant :

* *get*
* *set*
* *to*
* *object*

####Classes : 
Le nom des classes devrait être en CamelCase avec le premier caractère toujours en majuscule.

####Interfaces : 
Identique aux classes

####Méthodes : 
Le nom des méthodes devrait représenter une action et s'écrire en camelCase avec le premier caractère toujours en minuscule.

####Variables :
Le nom des variables devrait être en camelCase avec le premier caractère en minuscule. Le nom devrait être représentatif de ce que représente la variable. Les nom à un seul caractère ne devraient être utilisé que pour une utilisation temporaire. 

####Constantes :
Le nom des constantes devrait être en SNAKE_CASE avec tout les caractères en majuscule



[1]:http://www.oracle.com/technetwork/java/codeconventions-150003.pdf