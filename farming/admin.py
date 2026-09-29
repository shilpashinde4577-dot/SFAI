from django.contrib import admin
from django.contrib.auth.admin import UserAdmin as BaseUserAdmin
from django.contrib.auth.models import User
from .models import UserProfile

# Unregister the default User admin and register a custom one
admin.site.unregister(User)

class UserProfileInline(admin.StackedInline):
    model = UserProfile
    can_delete = False
    verbose_name_plural = 'Profile'

class CustomUserAdmin(BaseUserAdmin):
    inlines = [UserProfileInline]
    actions = ['make_admin']

    def make_admin(self, request, queryset):
        """Custom admin action to promote selected users to admin."""
        queryset.update(is_staff=True, is_superuser=True)
    make_admin.short_description = "Mark selected users as admin"

class UserProfileInline(admin.StackedInline):
    model = UserProfile
    can_delete = False
    verbose_name_plural = 'Profile'


admin.site.register(User, CustomUserAdmin)

@admin.register(UserProfile)
class UserProfileAdmin(admin.ModelAdmin):
    list_display = ('user', 'role', 'status')
    list_filter = ('role', 'status')
    search_fields = ('user__username', 'user__email')

    def save_model(self, request, obj, form, change):
        """If role is admin, ensure the linked User has admin privileges."""
        super().save_model(request, obj, form, change)
        if obj.role == 'admin':
            user = obj.user
            user.is_staff = True
            user.is_superuser = False  # you may adjust as needed
            user.save()

# Register any other models as needed
