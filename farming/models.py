from django.db import models
from django.contrib.auth.models import User
from django.db.models.signals import post_save
from django.dispatch import receiver


class UserProfile(models.Model):
    """Extends Django User with role, status, profile picture, and contact info."""
    ROLE_CHOICES = (
        ('user', 'User'),
        ('admin', 'Admin'),
    )
    STATUS_CHOICES = (
        ('active', 'Active'),
        ('inactive', 'Inactive'),
        ('banned', 'Banned'),
    )

    user = models.OneToOneField(User, on_delete=models.CASCADE, related_name='profile')
    role = models.CharField(max_length=20, choices=ROLE_CHOICES, default='user')
    status = models.CharField(max_length=20, choices=STATUS_CHOICES, default='active')
    profile_picture = models.ImageField(upload_to='profile_pics/', blank=True, null=True)
    phone_number = models.CharField(max_length=20, blank=True, null=True)
    state = models.CharField(max_length=100, blank=True, null=True)
    created_at = models.DateTimeField(auto_now_add=True)

    def __str__(self):
        return f"{self.user.username} ({self.role.upper()}) - {self.status.capitalize()}"

    @property
    def is_admin(self):
        return self.role == 'admin' or self.user.is_staff or self.user.is_superuser


@receiver(post_save, sender=User)
def create_or_update_user_profile(sender, instance, created, **kwargs):
    """Automatically create or update UserProfile when User is saved."""
    if created:
        role = 'admin' if instance.is_superuser or instance.is_staff else 'user'
        UserProfile.objects.create(user=instance, role=role)
    else:
        if hasattr(instance, 'profile'):
            instance.profile.save()
        else:
            role = 'admin' if instance.is_superuser or instance.is_staff else 'user'
            UserProfile.objects.create(user=instance, role=role)


class UserActivityLog(models.Model):
    """Tracks key user actions and platform activities."""
    user = models.ForeignKey(User, on_delete=models.CASCADE, related_name='activity_logs')
    action = models.CharField(max_length=255)
    details = models.TextField(blank=True, null=True)
    ip_address = models.GenericIPAddressField(blank=True, null=True)
    timestamp = models.DateTimeField(auto_now_add=True)

    class Meta:
        ordering = ['-timestamp']

    def __str__(self):
        return f"[{self.timestamp.strftime('%Y-%m-%d %H:%M')}] {self.user.username}: {self.action}"


class CropPrediction(models.Model):
    """Stores crop prediction history."""
    user = models.ForeignKey(User, on_delete=models.SET_NULL, null=True, blank=True, related_name='crop_predictions')
    temperature = models.FloatField()
    humidity = models.FloatField()
    soil_moisture = models.FloatField()
    predicted_crop = models.CharField(max_length=100)
    created_at = models.DateTimeField(auto_now_add=True)

    def __str__(self):
        return f"{self.predicted_crop} (Temp:{self.temperature}, Hum:{self.humidity}, Moist:{self.soil_moisture})"

    class Meta:
        ordering = ['-created_at']


class DiseaseDetection(models.Model):
    """Stores disease detection history from leaf images."""
    user = models.ForeignKey(User, on_delete=models.SET_NULL, null=True, blank=True, related_name='disease_detections')
    image = models.ImageField(upload_to='leaf_images/')
    detected_disease = models.CharField(max_length=200, blank=True, null=True)
    confidence = models.FloatField(blank=True, null=True)
    recommendation = models.TextField(blank=True, null=True)
    created_at = models.DateTimeField(auto_now_add=True)

    def __str__(self):
        return f"{self.detected_disease or 'Pending'} - {self.created_at.strftime('%Y-%m-%d %H:%M')}"

    class Meta:
        ordering = ['-created_at']


class FertilizerRecommendation(models.Model):
    """Stores fertilizer recommendation history."""
    user = models.ForeignKey(User, on_delete=models.SET_NULL, null=True, blank=True, related_name='fertilizer_recommendations')
    crop_name = models.CharField(max_length=100)
    nitrogen = models.FloatField()
    phosphorus = models.FloatField()
    potassium = models.FloatField()
    soil_ph = models.FloatField()
    recommendation = models.TextField()
    created_at = models.DateTimeField(auto_now_add=True)

    def __str__(self):
        return f"Fertilizer for {self.crop_name} - {self.created_at.strftime('%Y-%m-%d %H:%M')}"

    class Meta:
        ordering = ['-created_at']


class WeatherPrediction(models.Model):
    """Stores weather prediction history."""
    user = models.ForeignKey(User, on_delete=models.SET_NULL, null=True, blank=True, related_name='weather_predictions')
    temperature = models.FloatField()
    humidity = models.FloatField()
    soil_moisture = models.FloatField()
    predicted_weather = models.CharField(max_length=100)
    created_at = models.DateTimeField(auto_now_add=True)

    def __str__(self):
        return f"{self.predicted_weather} (Temp:{self.temperature}, Hum:{self.humidity}, Moist:{self.soil_moisture})"

    class Meta:
        ordering = ['-created_at']


class MarketAnalysis(models.Model):
    """Stores market analysis queries."""
    user = models.ForeignKey(User, on_delete=models.SET_NULL, null=True, blank=True, related_name='market_analyses')
    crop_name = models.CharField(max_length=100)
    state = models.CharField(max_length=100)
    current_price = models.FloatField()
    created_at = models.DateTimeField(auto_now_add=True)

    def __str__(self):
        return f"{self.crop_name} in {self.state} @ ₹{self.current_price}/kg"

    class Meta:
        ordering = ['-created_at']


class CropCompensation(models.Model):
    """Stores crop loss compensation queries."""
    user = models.ForeignKey(User, on_delete=models.SET_NULL, null=True, blank=True, related_name='crop_compensations')
    state = models.CharField(max_length=100)
    crop_name = models.CharField(max_length=100)
    damage_factor = models.CharField(max_length=100)
    acreage = models.FloatField()
    compensation_per_acre = models.FloatField()
    total_compensation = models.FloatField()
    created_at = models.DateTimeField(auto_now_add=True)

    def __str__(self):
        return f"{self.crop_name} - {self.state} ({self.damage_factor}) | ₹{self.total_compensation:.0f}"

    class Meta:
        ordering = ['-created_at']


class CropLossEligibility(models.Model):
    """Stores government crop loss benefit eligibility queries."""
    user = models.ForeignKey(User, on_delete=models.SET_NULL, null=True, blank=True, related_name='crop_eligibilities')
    state = models.CharField(max_length=100)
    crop_name = models.CharField(max_length=100)
    damage_factor = models.CharField(max_length=100)
    acreage = models.FloatField()
    loss_percentage = models.FloatField()
    intimation_hours = models.IntegerField()
    insured_status = models.CharField(max_length=50)
    farmer_category = models.CharField(max_length=100)
    is_eligible = models.BooleanField()
    confidence = models.FloatField()
    created_at = models.DateTimeField(auto_now_add=True)

    def __str__(self):
        status = "ELIGIBLE" if self.is_eligible else "NOT ELIGIBLE"
        return f"{self.crop_name} ({self.state}) - {status} ({self.confidence:.1f}%)"

    class Meta:
        ordering = ['-created_at']


