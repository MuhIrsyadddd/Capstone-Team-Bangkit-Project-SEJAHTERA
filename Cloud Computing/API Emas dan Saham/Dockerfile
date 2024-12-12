# Gunakan base image Python 3.10-slim untuk mengurangi ukuran image
FROM python:3.10-slim

# Set environment variables agar output Python tidak di-buffer
ENV PYTHONUNBUFFERED=1

# Tentukan direktori kerja dalam container
WORKDIR /app

# Salin file requirements.txt ke direktori kerja
COPY requirements.txt /app/

# Install dependencies dari requirements.txt tanpa menyimpan cache pip
RUN pip install --no-cache-dir -r requirements.txt

# Salin semua file dan folder dari proyek lokal ke direktori kerja dalam container
COPY . /app/

# Buka port 8080 untuk menerima koneksi dari luar container
EXPOSE 8080

# Jalankan aplikasi menggunakan Uvicorn
CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8080"]
