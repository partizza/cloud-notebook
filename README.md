### The project is example of microservices architecture on AWS Cloud.
It provides API to store and transcribe audio files. 
OpenAI model is used to transcript audio.

#
> [!TIP]
> **To start:**
- add OpenAI token to env variables
```shell
export OPENAI_API_KEY=your-toke-here
```

- build and start containers
```shell
docker-compose up --build
```

#
> [!TIP]
> **Save and transcribe audio:**
```shell
curl http://localhost:8080/audio -F file=@your_audio_file_path
```