# Subs Buddy

Your subtitle translating pal. Uploads an English .srt file and translates it to Bulgarian using DeepL API.

This application has been developed as a practical exercise for the CI/CD course lectured by Dave Farley. It follows the
TDD, DDD and CI/CD principles. The main task for this is the Github actions pipeline which follows the CI/CD principles
and recommendations as described by Dave.

Link to the course: https://courses.cd.training/collections

# Translation Tool Usage

The .srt translation tool will be separated into a dedicated maven module in the future. This will allow its usage
regardless of the use context.

# Webapp Deployment

## Prerequisites

- PostgreSQL database server installed and running
- DeepL API key (get one from https://developers.deepl.com/docs/getting-started/intro)

## Local Deployment

The following environment variables need to be configured:

- `AUTH_KEY` - Authentication key for the application's basic auth default user `subs-buddy`
- `DEEPL_API_KEY` - Your DeepL API key
- `DB_URL` - PostgreSQL database URL
- `DB_USER` - Database username
- `DB_PASS` - Database password

## GitHub Actions Deployment Pipeline

The application uses GitHub Actions for CI/CD pipeline automation. The workflow automatically builds, tests and deploys
the application when changes are pushed to the main branch. In addition to the variables needed for local deployment,
the following environment variables need to be configured:

- `DOCKER_USERNAME` - Docker Hub username for pushing images
- `DOCKER_PASSWORD` - Docker Hub access token
- `VPS_IP` - Deployment server IP address
- `VPS_PASS` - VPS password required for SSH access

For more details check the [deployment pipeline](.github/workflows/deployment-pipeline.yml) file.