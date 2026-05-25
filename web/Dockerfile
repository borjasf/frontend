# Imagen base
FROM node:18-alpine

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar archivos de dependencias
COPY package*.json ./

# Instalar dependencias de producción
RUN npm ci --omit=dev

# Copiar el resto del código
COPY . .
RUN mkdir -p public/uploads

# Exponer el puerto de la aplicación
EXPOSE 3001

# Comando de arranque
CMD ["node", "server.js"]