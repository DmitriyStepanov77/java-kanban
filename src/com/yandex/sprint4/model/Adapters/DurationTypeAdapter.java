package com.yandex.sprint4.model.Adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Duration;

public class DurationTypeAdapter extends TypeAdapter<Duration> {

    @Override
    public void write(final JsonWriter jsonWriter, final Duration duration) throws IOException {
        if (duration != null)
            jsonWriter.value(duration.toSeconds());
        else
            jsonWriter.value((String) null);
    }

    @Override
    public Duration read(final JsonReader jsonReader) throws IOException {
        var peek = jsonReader.peek();
        if (peek == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        }
        return Duration.parse("PT" + jsonReader.nextString() + "S");
    }
}
