# Regras ProGuard/R8. minify está desligado por padrão neste esqueleto.
# Ao ativar isMinifyEnabled = true, mantenha os serializadores gerados
# pela kotlinx.serialization:
-keepclassmembers class **$$serializer { *; }
-keepclasseswithmembers class kotlinx.serialization.** { *; }
