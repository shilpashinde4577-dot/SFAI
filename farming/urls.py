from django.urls import path
from . import views

app_name = 'farming'

urlpatterns = [
    path('', views.index, name='index'),
    path('register/', views.register_view, name='register'),
    path('login/', views.login_view, name='login'),
    path('logout/', views.logout_view, name='logout'),
    path('profile/', views.profile_view, name='profile'),
    path('crop-recommendation/', views.crop_recommendation, name='crop_recommendation'),
    path('disease-detection/', views.disease_detection, name='disease_detection'),
    path('fertilizer-recommendation/', views.fertilizer_recommendation, name='fertilizer_recommendation'),
    path('weather-prediction/', views.weather_prediction, name='weather_prediction'),
    path('market-analysis/', views.market_analysis, name='market_analysis'),
    path('crop-compensation/', views.crop_compensation, name='crop_compensation'),
    path('benefit-eligibility/', views.crop_loss_eligibility, name='benefit_eligibility'),
    path('api/get-compensation-rate/', views.api_get_compensation_rate, name='api_get_compensation_rate'),
    path('combined-report/', views.combined_report, name='combined_report'),
    path('export-pdf/<str:module_name>/', views.export_pdf_report, name='export_pdf_report'),
    path('export-pdf-combined/', views.export_combined_pdf, name='export_combined_pdf'),
    path('admin-dashboard/', views.admin_dashboard, name='admin_dashboard'),
    path('admin-dashboard/user/<int:user_id>/', views.admin_user_detail, name='admin_user_detail'),
    path('admin-dashboard/toggle-status/<int:user_id>/', views.admin_toggle_user_status, name='admin_toggle_user_status'),
    path('admin-dashboard/toggle-role/<int:user_id>/', views.admin_toggle_user_role, name='admin_toggle_user_role'),
    path('help/', views.help_section, name='help'),
]
