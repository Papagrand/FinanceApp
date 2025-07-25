package ru.point.settings.appInfo.domain

import javax.inject.Inject
import ru.point.api.repository.SettingsRepository

internal class GetAppVersionUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
) {

    operator fun invoke() = settingsRepository.getAppVersion()
}