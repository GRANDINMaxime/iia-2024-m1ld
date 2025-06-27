using commentaire_service.Startup;

var builder = WebApplication.CreateBuilder(args);

builder.Services.AddControllers();

builder.Services.UseDbContextConfiguration(builder.Configuration.GetConnectionString("CommentaireContext"));

builder.Services.UseEurekaConfiguration();
builder.Services.UseHttpConfiguration("produit-service", "lb://produit-service");

builder.Services.UseRabbitConfiguration(builder.Configuration);

var app = builder.Build();

app.UseHttpsRedirection();
app.MapControllers();

app.Run();
