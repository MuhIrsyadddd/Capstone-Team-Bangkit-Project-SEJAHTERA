# Use Python base image
FROM python:3.8-slim

# Set working directory
WORKDIR /app

# Copy pyproject.toml to the container
COPY pyproject.toml /app/pyproject.toml

# Install poetry to handle dependencies
RUN pip install poetry

# Install project dependencies
RUN poetry install

# Copy the rest of the application files
COPY . /app

# Expose the port Flask will run on
EXPOSE 5000

# Set the Flask environment variables
ENV FLASK_APP=main.py
ENV FLASK_ENV=production

# Command to run the Flask app
CMD ["poetry", "run", "flask", "run", "--host=0.0.0.0"]