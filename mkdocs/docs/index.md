# Welcome to MkDocs

For full documentation visit [mkdocs.org](https://www.mkdocs.org).

## Commands

* `mkdocs new [dir-name]` - Create a new project.
* `mkdocs serve` - Start the live-reloading docs server.
* `mkdocs build` - Build the documentation site.
* `mkdocs -h` - Print help message and exit.

## Project layout

    mkdocs.yml    # The configuration file.
    docs/
        index.md  # The documentation homepage.
        ...       # Other markdown pages, images and other files.

## Installation

To install MkDocs, run the following command from the command line:

```
pip install mkdocs
```

### Material Theme

Material for MkDocs can be installed with pip:

```
pip install mkdocs-material
```

Add the following lines to mkdocs.yml:

```
theme:
    name: material
```

## Environmental Variables

### Backend

```
NEO4J_HOST=
NEO4J_PORT=
NEO4J_USERNAME=
NEO4J_PASSWORD=
```

### Frontend

```
VITE_BACKEND_IP=
VITE_BACKEND_PORT=
VITE_BACKEND_MAP_API_KEY=
VITE_BACKEND_WEATHER_API_KEY=
```