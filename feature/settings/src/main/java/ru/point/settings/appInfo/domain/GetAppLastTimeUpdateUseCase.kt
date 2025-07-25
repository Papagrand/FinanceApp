package ru.point.settings.appInfo.domain

import javax.inject.Inject
import ru.point.api.repository.SettingsRepository

internal class GetAppLastTimeUpdateUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
) {

    operator fun invoke() = settingsRepository.getAppLastTimeUpdate()
}