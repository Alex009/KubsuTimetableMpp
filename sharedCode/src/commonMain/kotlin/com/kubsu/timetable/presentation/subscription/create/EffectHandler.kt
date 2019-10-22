package com.kubsu.timetable.presentation.subscription.create

import com.egroden.teaco.EffectHandler
import com.egroden.teaco.fold
import com.kubsu.timetable.data.storage.displayed.subscription.DisplayedSubscriptionStorage
import com.kubsu.timetable.domain.interactor.subscription.SubscriptionInteractor
import com.kubsu.timetable.extensions.checkWhenAllHandled
import com.kubsu.timetable.presentation.subscription.mapper.FacultyModelMapper
import com.kubsu.timetable.presentation.subscription.mapper.GroupModelMapper
import com.kubsu.timetable.presentation.subscription.mapper.OccupationModelMapper
import com.kubsu.timetable.presentation.subscription.mapper.SubgroupModelMapper
import com.kubsu.timetable.presentation.timetable.mapper.SubscriptionModelMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/*
HttpClient: REQUEST: https://kubsu-timetable.info/api/v1/subscriptions/
HttpClient: METHOD: HttpMethod(value=POST)
HttpClient: COMMON HEADERS
HttpClient: -> sessionid: rkkt7eocb0kssadlu1md8ttwerd8qlm6
HttpClient: -> Accept: application/json
HttpClient: -> Accept-Charset: UTF-8
HttpClient: CONTENT HEADERS
HttpClient: BODY Content-Type: application/json
HttpClient: BODY START
HttpClient: {"subgroup":1,"title":"ФКТиПМ Моиаис 35/1","is_main":false}
HttpClient: BODY END
HttpClient: RESPONSE: 201 Created
HttpClient: METHOD: HttpMethod(value=POST)
HttpClient: FROM: https://kubsu-timetable.info/api/v1/subscriptions/
HttpClient: COMMON HEADERS
HttpClient: -> Allow: GET, POST, HEAD, OPTIONS
HttpClient: -> Connection: keep-alive
HttpClient: -> Content-Length: 79
HttpClient: -> Content-Type: application/json
HttpClient: -> Date: Tue, 22 Oct 2019 10:02:55 GMT
HttpClient: -> Server: nginx/1.14.0 (Ubuntu)
HttpClient: -> Vary: Cookie, Origin
HttpClient: -> X-Android-Received-Millis: 1571738576523
HttpClient: -> X-Android-Response-Source: NETWORK 201
HttpClient: -> X-Android-Selected-Protocol: http/1.1
HttpClient: -> X-Android-Sent-Millis: 1571738576301
HttpClient: -> X-Frame-Options: SAMEORIGIN
HttpClient: BODY Content-Type: application/json
HttpClient: BODY START
HttpClient: {"id":18,"title":"ФКТиПМ Моиаис 35/1","subgroup":1,"is_main":false}
HttpClient: BODY END
HttpClient: REQUEST: https://kubsu-timetable.info/api/v1/timetables/?subgroup_id=18
HttpClient: METHOD: HttpMethod(value=GET)
HttpClient: COMMON HEADERS
HttpClient: -> Accept: application/json
HttpClient: -> Accept-Charset: UTF-8
HttpClient: CONTENT HEADERS
HttpClient: BODY Content-Type: null
HttpClient: BODY START
HttpClient: BODY END
HttpClient: RESPONSE: 200 OK
HttpClient: METHOD: HttpMethod(value=GET)
HttpClient: FROM: https://kubsu-timetable.info/api/v1/timetables/?subgroup_id=18
HttpClient: COMMON HEADERS
HttpClient: -> Allow: GET, HEAD, OPTIONS
HttpClient: -> Connection: keep-alive
HttpClient: -> Content-Length: 2
HttpClient: -> Content-Type: application/json
HttpClient: -> Date: Tue, 22 Oct 2019 10:02:55 GMT
HttpClient: -> Server: nginx/1.14.0 (Ubuntu)
HttpClient: -> Vary: Cookie, Origin
HttpClient: -> X-Android-Received-Millis: 1571738576798
HttpClient: -> X-Android-Response-Source: NETWORK 200
HttpClient: -> X-Android-Selected-Protocol: http/1.1
HttpClient: -> X-Android-Sent-Millis: 1571738576631
HttpClient: -> X-Frame-Options: SAMEORIGIN
HttpClient: BODY Content-Type: application/json
HttpClient: BODY START
HttpClient: []
HttpClient: BODY END
 */

class CreateSubscriptionEffectHandler(
    private val interactor: SubscriptionInteractor,
    private val displayedSubscriptionStorage: DisplayedSubscriptionStorage
) : EffectHandler<SideEffect, Action> {
    override fun invoke(sideEffect: SideEffect): Flow<Action> = flow {
        when (sideEffect) {
            SideEffect.SelectFacultyList ->
                interactor
                    .selectFacultyList()
                    .fold(
                        ifLeft = { emit(Action.ShowDataFailure(listOf(it))) },
                        ifRight = {
                            emit(
                                Action.FacultyListUploaded(
                                    it.map(FacultyModelMapper::toModel)
                                )
                            )
                        }
                    )

            is SideEffect.SelectOccupationList ->
                interactor
                    .selectOccupationList(
                        FacultyModelMapper.toEntity(sideEffect.faculty)
                    )
                    .fold(
                        ifLeft = { emit(Action.ShowDataFailure(listOf(it))) },
                        ifRight = {
                            emit(
                                Action.OccupationListUploaded(
                                    it.map(OccupationModelMapper::toModel)
                                )
                            )
                        }
                    )

            is SideEffect.SelectGroupList ->
                interactor
                    .selectGroupList(
                        OccupationModelMapper.toEntity(sideEffect.occupation)
                    )
                    .fold(
                        ifLeft = { emit(Action.ShowDataFailure(listOf(it))) },
                        ifRight = {
                            emit(
                                Action.GroupListUploaded(
                                    it.map(GroupModelMapper::toModel)
                                )
                            )
                        }
                    )

            is SideEffect.SelectSubgroupList ->
                interactor
                    .selectSubgroupList(
                        GroupModelMapper.toEntity(sideEffect.group)
                    )
                    .fold(
                        ifLeft = { emit(Action.ShowDataFailure(listOf(it))) },
                        ifRight = {
                            emit(
                                Action.SubgroupListUploaded(
                                    it.map(SubgroupModelMapper::toModel)
                                )
                            )
                        }
                    )

            is SideEffect.CreateSubscription ->
                interactor
                    .create(
                        subgroupId = sideEffect.subgroup.id,
                        subscriptionName = sideEffect.subscriptionName,
                        isMain = sideEffect.isMain
                    )
                    .fold(
                        ifLeft = { requestFailure ->
                            requestFailure.handle(
                                ifDomain = { emit(Action.ShowSubscriptionFailure(it)) },
                                ifData = { emit(Action.ShowDataFailure(it)) }
                            )
                        },
                        ifRight = {
                            emit(
                                Action.SubscriptionWasCreated(
                                    SubscriptionModelMapper.toModel(it)
                                )
                            )
                        }
                    )

            is SideEffect.DisplayedSubscription ->
                displayedSubscriptionStorage.set(sideEffect.subscription)
        }.checkWhenAllHandled()
    }
}